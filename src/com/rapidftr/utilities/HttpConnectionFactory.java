package com.rapidftr.utilities;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.RadioInfo;


/**
 * @author Marcus Watkins (marcus@versatilemonkey.com)
 * @version 1.0 (June 24, 2009) 
 * http://www.versatilemonkey.com<p><p>
 * 
 * This code is public domain, do with it whatever you wish.
 * <p>
 * This class aims to simplify HTTP requests on the BlackBerry platform. It could be extended
 * for other uses (socket, specifically), but that exercise is left up to the reader.<p>
 * Use of this class will require signing your application using RIM supplied signing keys.
 * <p>
 * The BlackBerry platform provides a multitude of different transports for network access. These include WiFi, BES, BIS, WAP2 and Direct TCP.
 * <p>
 * Not all transports are available on all devices, carriers or service plans. Ordinarily an application must determine on its own which 
 * transports are available for a given device, and attempt to connect via them in order.
 * 
 * Sample use:<p>
 * <pre>
 * <code>
 * HttpConnectionFactory factory = new HttpConnectionFactory( "http://www.versatilemonkey.com/test.txt", HttpConnectionFactory.TRANSPORT_WIFI | HttpConnectionFactory.TRANSPORT_BES );
 * while( true ) {
 *    try {
 *       HttpConnection connection = factory.getNextConnection();
 *       try {
 *          connection.setRequestMethod( "POST" );
 *          connection.setRequestProperty( "Content-type", "application/x-www-form-urlencoded" );
 *          OutputStream os = connection.openOutputStream( );
 *          os.write( "foo=bar&var2=val2".getBytes() );
 *          os.close();
 *          InputStream is = connection.openInputStream();
 *          //do something with the input stream
 *          if( whatever we did worked ) {
 *             break;
 *          }
 *       }
 *       catch( IOException ) {
 *          //Log the error or store it for displaying to the end user if no transports succeed
 *       }
 *    }
 *    catch( NoMoreTransportsException e ) {
 *       //There are no more transports to attempt
 *       Dialog.alert( "Unable to perform request" ); //Note you should never attempt network activity on the event thread
 *       break;
 *    }
 * }
 * </code>
 * </pre>
 * <p>
 * It is possible that an HttpConnection returned by onnection will fail in a method undetectable before attemping the request.
 * <p>
 * Notes about the various transports from my experience:
 * <p>
 * <b>Wifi:</b>
 * <p>
 * Wifi is the least cost to the user and is also the fasted by orders of magnitude
 * <p><p>
 * <b>BES/BIS:</b>
 * <p>
 * - These are largely the same, except BES goes through the user's corporate network and may be subject to corporate firewalls<p>
 * - BES/BIS are generally offered as unlimited use to anyone with a BlackBerry specific data plan<p>
 * - There is usually a limit imposed on the size of files that can be retrieved (usually 5mb, but can be as low as 100kb)<p>
 * <p>
 * <b>Direct TCP and WAP2:</b><p>
 * - These transports use carrier data plans which are sometimes billed at the same rate as BES/BIS, but sometimes are billed
 * separately. It is possible for users to be on unlimited data plans via BES/BIS but be charged per MB for TCP and WAP2. I
 * have never seen the reverse, however.<p>
 * - Some carriers do not have limits on the file size, others will timeout if you request a file over their limit (instead of a 413 error or similar)<p>
 * - Some carriers have intermediary proxies that will alter the content of returned files (wrapping them in carrier specific content, for example)<p>
 * <p>
 * <p>
 * Good luck, I hope this makes networking on BlackBerry easier for you.
 */
public class HttpConnectionFactory {

	
	/**
	 * Specifies that only wifi should be used
	 */
	public static final int TRANSPORT_WIFI = 1;
	
	/**
	 * Specifies that only BES (also known as MDS or corporate servers)
	 */
	public static final int TRANSPORT_BES = 2;
	
	/**
	 * Specifies that only BIS should be used (Basically RIM hosted BES)
	 */
	public static final int TRANSPORT_BIS = 4;
	
	/**
	 * Specifies that TCP should be used (carrier transport)
	 */
	public static final int TRANSPORT_DIRECT_TCP = 8;
	
	/**
	 * Specifies that WAP2 should be used (carrier transport)
	 */
	public static final int TRANSPORT_WAP2 = 16;
	
	/**
	 * Equivalent to: TRANSPORT_WIFI | TRANSPORT_BES | TRANSPORT_BIS | TRANSPORT_DIRECT_TCP | TRANSPORT_WAP2
	 */
	public static final int TRANSPORTS_ANY = TRANSPORT_WIFI | TRANSPORT_BES | TRANSPORT_BIS | TRANSPORT_DIRECT_TCP | TRANSPORT_WAP2;
	
	/**
	 * Equivalent to: TRANSPORT_WIFI | TRANSPORT_BES | TRANSPORT_BIS
	 */
	public static final int TRANSPORTS_AVOID_CARRIER = TRANSPORT_WIFI | TRANSPORT_BES | TRANSPORT_BIS;
	
	/**
	 * Equivalent to: TRANSPORT_DIRECT_TCP | TRANSPORT_WAP2
	 */
	public static final int TRANSPORTS_CARRIER_ONLY = TRANSPORT_DIRECT_TCP | TRANSPORT_WAP2;
	
	/**
	 * The default order in which selected transports will be attempted
	 * 
	 */
	public static final int DEFAULT_TRANSPORT_ORDER[] = { TRANSPORT_WIFI, TRANSPORT_BIS, TRANSPORT_DIRECT_TCP, TRANSPORT_BES, TRANSPORT_WAP2 };
	
	private static final int TRANSPORT_COUNT = DEFAULT_TRANSPORT_ORDER.length;
	
	private static ServiceRecord srMDS[], srBIS[], srWAP2[], srWiFi[];
	private static boolean serviceRecordsLoaded = false;
	
	private int curIndex = 0;
	private int curSubIndex = 0;
	private String url;
	private String extraParameters;
	private int transports[];
	private int lastTransport = 0;
	
	/**
	 * Equivalent to <code>HttpConnectionFactory( url, null, HttpConnectionFactory.DEFAULT_TRANSPORT_ORDER )</code>
	 * @see #HttpConnectionFactory(String, String, int[])
	 * @param url See {@link #HttpConnectionFactory(String, String, int[])}
	 */
	public HttpConnectionFactory( String url ) {
		this( url, null, 0 );
	}

	/**
	 * Equivalent to <code>HttpConnectionFactory( url, null, allowedTransports )</code>
	 * 
	 * @see #HttpConnectionFactory(String, String, int)
	 * @param url See {@link #HttpConnectionFactory(String, String, int)}
	 * @param allowedTransports See {@link #HttpConnectionFactory(String, String, int)}
	 */
	public HttpConnectionFactory( String url, int allowedTransports ) {
		this( url, null, allowedTransports );
	}
	/**
	 * Equivalent to <code>HttpConnectionFactory( url, null, transportPriority )</code>
	 * 
	 * @see #HttpConnectionFactory(String, String, int[])
	 * @param url See {@link #HttpConnectionFactory(String, String, int[])}
	 * @param transportPriority See {@link #HttpConnectionFactory(String, String, int[])}
	 */
	public HttpConnectionFactory( String url, int transportPriority[] ) {
		this( url, null, transportPriority );
	}

	/**
	 * Creates a factory specifying the target http/https url and mask containing which transports should be allowed (default order) 
	 * 
	 * This method converts allowedTransports into an array of transports to use arranging
	 * the included transports in the order specified by {@link #DEFAULT_TRANSPORT_ORDER}
	 * Once the translation is complete it is equivalent to calling:
	 * <p>
	 * <code>
	 * HttpConnectionFactory( String url, String extraParameters, int transportPriority[] );
	 * </code>
	 * <p>
	 * But only the transports matching the input mask are included in the array.
	 * 
	 * @param url See {@link #HttpConnectionFactory(String, String, int[])}
	 * @param extraParameters See {@link #HttpConnectionFactory(String, String, int[])}
	 * @param allowedTransports A set of transports that should be allowed, for example, to set only wifi and BES use: TRANSPORT_WIFI | TRANSPORT_BES
	 */
	public HttpConnectionFactory( String url, String extraParameters, int allowedTransports ) {
		this( url, extraParameters, transportMaskToArray( allowedTransports ) );
	}
	
	/**
	 * Creates a factory specifying the target http/https url and ordered list of transports to attempt
	 * 
	 * This method constructs an <code>HttpConnectionFactory</code> for the URL specified, using any extra connection parameters
	 * specified in <code>extraParemeters</code> that will follow the order of transports specified in <code>transportPriority</code>
	 * <p>
	 * Transports not in <code>transportPriority</code> are not used in any order. The order of transports attempted will follow the order the
	 * are presented in <code>transportPriority</code>
	 * 
	 * <code>extraParameters</code> are additional parameters you want added to the connection string, each must be preceded by a semicolon. These are some useful ones:<p>
	 * ConnectionTimeout=120000 (2 minute connection timeout)<p>
	 * EncryptRequired=true (No idea what this does)<p>
	 * Example: ";ConnectionTimeout=120000;EncryptRequired=true"
	 * See http://www.blackberryforums.com/developer-forum/113137-undocumented-connector-open-parameters.html for more info.
	 * 
	 * @param url The url to generate the HttpConnection for (only http and https)
	 * @param extraParameters Extra parameters that will get appended to the end of the final url used in {@link #javax.microedition.io.Connector.open( String ) }
	 * @param transportPriority An array of transports in the order they should be attempted
	 */
	public HttpConnectionFactory( String url, String extraParameters, int transportPriority[] ) {
		if( !serviceRecordsLoaded ) {
			loadServiceBooks( false );
		}
		
		if( url == null ) {
			throw new IllegalArgumentException( "Null URL passed in" );
		}
		if( !url.toLowerCase().startsWith( "http" ) ) {
			throw new IllegalArgumentException( "URL not http or https" );
		}
		
		this.url = url;
		this.extraParameters = extraParameters;
		transports = transportPriority;
	}
	
	
	/**
	 * Generates an HttpConnection using the next available transport according to the order specified during factory creation to
	 * the url specified in factory creation. See the class description for details on use.
	 * 
	 * @return An HttpConnection using the next transport configured during factory creation
	 * @throws NoMoreTransportsException No more transports are available to use
	 */
	public HttpConnection getNextConnection() throws NoMoreTransportsException {
		HttpConnection con = null;
		int curTransport = 0;
		while( con == null && curIndex < transports.length ) {
			curTransport = transports[curIndex];
			switch( curTransport ) {
				case TRANSPORT_WIFI:
					System.out.println("USING WIFI");
					
					curIndex++;
					curSubIndex = 0;
					try { con = getWifiConnection(); } catch( Exception e ) { }
					break;
				case TRANSPORT_BES:
					curIndex++;
					curSubIndex = 0;
					try { con = getBesConnection(); } catch( Exception e ) { }
					break;
				case TRANSPORT_BIS:
					System.out.println("USING BIS");
					while( con == null ) {
						try { 
							con = getBisConnection( curSubIndex++ );
						} 
						catch( NoMoreTransportsException e ) { 
							curIndex++;
							curSubIndex = 0;
							break;
						}
						catch( Exception e ) { }
					}
					break;
				case TRANSPORT_DIRECT_TCP:
					System.out.println("USING TCPIP");
					curIndex++;
					try { con = getTcpConnection(); } catch( Exception e ) { }
					break;
				case TRANSPORT_WAP2:
					while( con == null ) {
						try {
							con = getWap2Connection( curSubIndex++ );
						}
						catch( NoMoreTransportsException e ) {
							curIndex++;
							curSubIndex = 0;
							break;
						}
						catch( Exception e ) { }
					}
					break;
			}
		}
		if( con == null ) {
			throw new NoMoreTransportsException( );
		}
		
		lastTransport = curTransport;
		return con;
	}
	
	/**
	 * Returns the transport used in the connection last returned via {@link #getNextConnection()}
	 * @return the transport used in the connection last returned via {@link #getNextConnection()} or 0 if none
	 */
	public int getLastTransport() {
		return lastTransport;
	}
	
	/**
	 * Generates a connection using the BIS transport if available
	 * 
	 * @param index The index of the service book to use
	 * @return An {@link HttpConnection} if this transport is available, otherwise null
	 * @throws NoMoreTransportsException
	 * @throws IOException throws exceptions generated by {@link getConnection( String transportExtras1, String transportExtras2 )}
	 */
	private HttpConnection getBisConnection( int index ) throws NoMoreTransportsException, IOException {
		if( index >= srBIS.length ) {
			throw new NoMoreTransportsException( "Out of BIS transports" );
		}
		ServiceRecord sr = srBIS[index];
		return getConnection( ";deviceside=false;connectionUID=", sr.getUid() );
	}
	/**
	 * Generates a connection using the BES transport if available
	 * 
	 * @return An {@link HttpConnection} if this transport is available, otherwise null
	 * @throws IOException throws exceptions generated by {@link getConnection( String transportExtras1, String transportExtras2 )}
	 */
	private HttpConnection getBesConnection( ) throws IOException {
		if( CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_MDS) ) {
			return getConnection( ";deviceside=false", null );
		}
		return null;
	}
	/**
	 * Generates a connection using the WIFI transport if available
	 * 
	 * @return An {@link HttpConnection} if this transport is available, otherwise null
	 * @throws IOException throws exceptions generated by {@link getConnection( String transportExtras1, String transportExtras2 )}
	 */
	private HttpConnection getWifiConnection() throws IOException {
		if(    RadioInfo.areWAFsSupported( RadioInfo.WAF_WLAN )
		    && ( RadioInfo.getActiveWAFs() & RadioInfo.WAF_WLAN ) != 0
			&& CoverageInfo.isCoverageSufficient( 1 /* CoverageInfo.COVERAGE_DIRECT */, RadioInfo.WAF_WLAN, false) ) {
			
			return getConnection( ";deviceside=true;interface=wifi", null );
			
		}
		return null;
	}
	
	/**
	 * Generates a connection using the WAP2 transport if available
	 * 
	 * @param index The index of the service book to use
	 * @return An {@link HttpConnection} if this transport is available, otherwise null
	 * @throws NoMoreTransportsException if index is outside the range of available service books
	 * @throws IOException throws exceptions generated by {@link getConnection( String transportExtras1, String transportExtras2 )}
	 */
	private HttpConnection getWap2Connection( int index ) throws NoMoreTransportsException, IOException {
		if( index >= srWAP2.length ) {
			throw new NoMoreTransportsException( "Out of WAP2 transports" );
		}
		if( CoverageInfo.isCoverageSufficient( 1 /* CoverageInfo.COVERAGE_DIRECT*/ ) ) {
			ServiceRecord sr = srWAP2[index];
			return getConnection( ";deviceside=true;ConnectionUID=", sr.getUid() );
		}
		return null;
	}

	/**
	 * Generates a connection using the TCP transport if available
	 * 
	 * @return An {@link HttpConnection} if this transport is available, otherwise null
	 * @throws IOException throws exceptions generated by {@link getConnection( String transportExtras1, String transportExtras2 )}
	 */
	private HttpConnection getTcpConnection( ) throws IOException {
		if( CoverageInfo.isCoverageSufficient( 1 /* CoverageInfo.COVERAGE_DIRECT */ ) ) {
			return getConnection( ";deviceside=true", null );
		}
		return null;
	}
	
	/**
	 * Utility method for actually getting a connection using whatever transport arguments the transport may need
	 * 
	 * @param transportExtras1 If not null will be concatenated onto the end of the {@link url}
	 * @param transportExtras2 If not null will be concatenated onto the end of {@link url} after transportExtras1
	 * @return An {@link HttpConnection} built using the url and transport settings provided
	 * @throws IOException any exceptions thrown by {@link Connector.open( String name )}
	 */
	private HttpConnection getConnection( String transportExtras1, String transportExtras2 ) throws IOException {
		StringBuffer fullUrl = new StringBuffer( );
		fullUrl.append( url );
		if( transportExtras1 != null ) {
			fullUrl.append( transportExtras1 );
		}
		if( transportExtras2 != null ) {
			fullUrl.append( transportExtras2 );
		}
		if( extraParameters != null ) {
			fullUrl.append( extraParameters );
		}
		return (HttpConnection)Connector.open( fullUrl.toString() );
	}


	/**
	 * Public method used to reload service books for whatever reason (though I can't think of any)
	 */
	public static void reloadServiceBooks() {
		loadServiceBooks( true );
	}
	
	/**
	 * Loads all pertinent service books into local variables for later use. Called upon first instantiation of the class and upload {@link reloadServiceBooks()}
	 * @param reload Whether to force a reload even if they've already been loaded.
	 */
	private static synchronized void loadServiceBooks( boolean reload ) {
		if( serviceRecordsLoaded && !reload ) {
			return;
		}
		ServiceBook sb = ServiceBook.getSB();
		ServiceRecord[] records = sb.getRecords();
		Vector mdsVec = new Vector();
		Vector bisVec = new Vector();
		Vector wap2Vec = new Vector();
		Vector wifiVec = new Vector();
		
		
        if( !serviceRecordsLoaded ) {
    		for (int i = 0; i < records.length; i++) {
    			ServiceRecord myRecord = records[i];
    			String cid, uid;

    			if (myRecord.isValid() && !myRecord.isDisabled()) {
    				cid = myRecord.getCid().toLowerCase();
    				uid = myRecord.getUid().toLowerCase();
    				// BIS
    				if (cid.indexOf("ippp") != -1 && uid.indexOf("gpmds") != -1) {
    					bisVec.addElement( myRecord );
    				}
    				// WAP1.0: Not implemented.

    				// BES
    				if (cid.indexOf("ippp") != -1 && uid.indexOf("gpmds") == -1) {
    					mdsVec.addElement( myRecord );
    				}
    				// WiFi
    				if (cid.indexOf("wptcp") != -1 && uid.indexOf("wifi") != -1) {
    					wifiVec.addElement( myRecord );
    				}
    				// Wap2
    				if (cid.indexOf("wptcp") != -1 && uid.indexOf("wap2") != -1) {
    					wap2Vec.addElement( myRecord );
    				}
    			}
    		}
    		srMDS = new ServiceRecord[mdsVec.size()];
    		mdsVec.copyInto( srMDS );
    		mdsVec.removeAllElements();
    		mdsVec = null;
    		
    		srBIS = new ServiceRecord[bisVec.size()];
    		bisVec.copyInto( srBIS );
    		bisVec.removeAllElements();
    		bisVec = null;
    		
    		srWAP2 = new ServiceRecord[wap2Vec.size()];
    		wap2Vec.copyInto( srWAP2 );
    		wap2Vec.removeAllElements();
    		wap2Vec = null;
    		
    		srWiFi = new ServiceRecord[wifiVec.size()];
    		wifiVec.copyInto( srWiFi );
    		wifiVec.removeAllElements();
    		wifiVec = null;
    		
    		serviceRecordsLoaded = true;
        }
	}

	/**
	 * Utility methd for converting a mask of transports into an array of transports in default order
	 * @param mask ORed collection of masks, example: <code>TRANSPORT_WIFI | TRANSPORT_BES</code>
	 * @return an array of the transports specified in mask in default order, example: { TRANSPORT_WIFI, TRANSPORT_BES }
	 */
	private static int[] transportMaskToArray( int mask ) {
		if( mask == 0 ) {
			mask = TRANSPORTS_ANY;
		}
		int numTransports = 0;
		for( int i = 0; i < TRANSPORT_COUNT; i++ ) {
			if( ( DEFAULT_TRANSPORT_ORDER[i] & mask ) != 0 ) {
				numTransports++;
			}
		}
		int transports[] = new int[numTransports];
		int index = 0;
		for( int i = 0; i < TRANSPORT_COUNT; i++ ) {
			if( ( DEFAULT_TRANSPORT_ORDER[i] & mask ) != 0 ) {
				transports[index++] = DEFAULT_TRANSPORT_ORDER[i];
			}
		}
		return transports;
	}
}
