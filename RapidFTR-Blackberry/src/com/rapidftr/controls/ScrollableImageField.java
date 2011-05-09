package com.rapidftr.controls;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.KeypadListener;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;


/**
 * A custom field that allows for scrolling of an image that is larger than the field can display at one time.
 */

public class ScrollableImageField extends BitmapField {
    private Bitmap theImage;       //The image to display.
    private int preferredHeight;   //The height the field request.
    private int preferredWidth;    //The width the field requests.

    private int xCoord = 0;         //The x coordinate for the top left corner of the image.
    private int yCoord = 0;         //The y coordinate for the top left corner of the image.

    private static final int[] COLOURS = {Color.WHITE, Color.GRAY, Color.GRAY, Color.WHITE};  //Colours used to draw the scroll arrows.

    private static final int TRIANGLE_SIZE = 6;     //The scroll arrow size.
    private static final int TRIANGLE_OFFSET = 1;   //The scroll arrow offset from the edge of the focus bar.

    private MenuItem helpMenu;         //The help MenuItem for this field.
    private int imageYCord;

    public ScrollableImageField(Bitmap theImage, int imageYCord) {
        super(theImage, BitmapField.FOCUSABLE);
        this.imageYCord = imageYCord;
        this.theImage = theImage;
        calculateSize();

        //Initialize the help MenuItem.
        helpMenu = new MenuItem("Image Help", 40, 40) {
            public void run() {
                Dialog.alert("Scroll the trackwheel to view the image.  Scroll to the end of the image or hold down Alt while scrolling to leave the field.");
            }
        };
    }

    public ScrollableImageField(EncodedImage theImage) {
        this(theImage.getBitmap(), 0);
    }

    //Calculates the preferred width and height of the field and determines if the image should scroll. 

    private void calculateSize() {

        //Set the preferred height to the image size or screen height if the image is larger than the screen height.
        int viewPortHeight = Display.getHeight() - imageYCord;
        int viewPortWidth = Display.getWidth();
        if (theImage.getHeight() > viewPortHeight) {
            preferredHeight = viewPortHeight;
        } else {
            preferredHeight = theImage.getHeight();
        }

        //Set the preferred width to the image size or screen width if the image is larger than the screen width.
        if (theImage.getWidth() > viewPortWidth) {
            preferredWidth = viewPortWidth;
        } else {
            preferredWidth = theImage.getWidth();
        }
    }

    //Override setBitmap to perform new size calculations.

    public void setBitmap(Bitmap bitmap) {
        super.setBitmap(bitmap);

        //Reset the image location to display the top left of the image.
        xCoord = 0;
        yCoord = 0;

        theImage = bitmap;
        calculateSize();
    }

    //Override setImage to perform new size calculations.

    public void setImage(EncodedImage image) {
        theImage = image.getBitmap();
        //Reset the image location to display the top left of the image.

        xCoord = 0;
        yCoord = 0;

        this.setBitmap(theImage);
        calculateSize();
    }

    public int getPreferredHeight() {
        return preferredHeight;
    }

    public int getPreferredWidth() {
        return preferredWidth;
    }

    protected void paint(Graphics graphics) {
        //Get the actual field size from its manager.
        XYRect rect = this.getManager().getContentRect();

        //Draw the Bitmap, taking up the full size of the BitmapField.
        //Start drawing the bitmap at the current coordinates.        
        graphics.drawBitmap(0, 0, rect.width, rect.height, theImage, xCoord, yCoord);
    }

    protected void drawFocus(Graphics graphics, boolean on) {
        if (!on)
            return;
        //Get the field size that is available from its manager.
        XYRect rect = this.getManager().getContentRect();
        graphics.setColor(Color.BLUE);

        int width = rect.width;
        int height = rect.height;

        //The manager may allocate more space than we need.
        //Make sure our dimensions are not greater than the image's.
        if (width > theImage.getWidth()) {
            width = theImage.getWidth();
        }

        if (height > theImage.getHeight()) {
            height = theImage.getHeight();
        }

        //Determine if the image is larger than the displayed area..
        if (width < theImage.getWidth() || height < theImage.getHeight()) {
            //It is, draw scrolling indicators.
            //All point paths start at the inner tip of the triangle and return back to it.
            int centerWidth = width / 2;
            int centerHeight = height / 2;

            //Draw the top triangle.
            int[] xPts = {centerWidth, centerWidth + TRIANGLE_SIZE, centerWidth - TRIANGLE_SIZE, centerWidth};
            int[] yPts = {TRIANGLE_OFFSET, TRIANGLE_OFFSET + TRIANGLE_SIZE, TRIANGLE_OFFSET + TRIANGLE_SIZE, TRIANGLE_OFFSET};

            graphics.drawShadedFilledPath(xPts, yPts, null, COLOURS, null);

            //Draw the bottom arrow.
            yPts[0] = height - TRIANGLE_OFFSET;
            yPts[1] = height - TRIANGLE_SIZE;
            yPts[2] = height - TRIANGLE_SIZE;
            yPts[3] = height - TRIANGLE_OFFSET;

            graphics.drawShadedFilledPath(xPts, yPts, null, COLOURS, null);

            //Draw the left arrow.
            xPts[0] = TRIANGLE_OFFSET;
            xPts[1] = TRIANGLE_OFFSET + TRIANGLE_SIZE;
            xPts[2] = TRIANGLE_OFFSET + TRIANGLE_SIZE;
            xPts[3] = TRIANGLE_OFFSET;

            yPts[0] = centerHeight;
            yPts[1] = centerHeight + TRIANGLE_SIZE;
            yPts[2] = centerHeight - TRIANGLE_SIZE;
            yPts[3] = centerHeight;

            graphics.drawShadedFilledPath(xPts, yPts, null, COLOURS, null);

            //Draw the right arrow.
            xPts[0] = width - TRIANGLE_OFFSET;
            xPts[1] = width - TRIANGLE_SIZE;
            xPts[2] = width - TRIANGLE_SIZE;
            xPts[3] = width - TRIANGLE_OFFSET;

            graphics.drawShadedFilledPath(xPts, yPts, null, COLOURS, null);
        }
    }

    //Override navigationMovement to provide image scrolling.

    protected boolean navigationMovement(int dx, int dy, int status, int time) {
        //Alt + scroll bypasses scrolling within the field.
        if ((status & KeypadListener.STATUS_ALT) != 0) {
            return super.navigationMovement(dx, dy, status, time);
        }

        if (scrollImage(dx * 5, dy * 5)) {
            //The image was scrolled and the movement consumed.
            return true;
        } else {
            //Scrolling is not required.
            return super.navigationMovement(dx, dy, status, time);
        }
    }


    //Determines if scrolling is required and handles the positioning of the image 
    //when scrolling occurs.  Returns true if the image was scrolled.

    private boolean scrollImage(int xScroll, int yScroll) {
        //Get the actual field size from its manager.
        XYRect rect = this.getManager().getContentRect();

        //Determine if the image is larger than the field.
        if (rect.width < theImage.getWidth() || rect.height < theImage.getHeight()) {
            //Image is larger than the field.  Enable scrolling support.
            xCoord += xScroll;
            yCoord += yScroll;

            //If the user has scrolled to the end of the image, use the default
            //navigationMovement to allow focus to scroll off the field.
            if (xCoord < 0) {
                xCoord = 0;
            }

            if (yCoord < 0) {
                yCoord = 0;
            }

            if (((theImage.getWidth() - xCoord) < rect.width) ||
                    ((theImage.getHeight() - yCoord) < rect.height)) {
                //Ensure the coordinates do not go lower than 0.

                //Ensure that we don't scroll beyond the image size (causing white space to be drawn).
                if ((theImage.getWidth() - xCoord) < rect.width) {
                    xCoord = theImage.getWidth() - rect.width;
                }

                if ((theImage.getHeight() - yCoord) < rect.height) {
                    yCoord = theImage.getHeight() - rect.height;
                }

                if (xCoord < 0) {
                    xCoord = 0;
                }

                if (yCoord < 0) {
                    yCoord = 0;
                }
                //Scrolling is not required.
                return false;
            } else {
                //The image was scrolled and the movement consumed.
                //Redraw the bitmap.
                this.invalidate();
                return true;
            }
        } else {
            //Scrolling is not required.
            return false;
        }
    }

    //Override makeContextMenu to add the field's help MenuItem.

    protected void makeContextMenu(ContextMenu contextMenu) {
        contextMenu.addItem(helpMenu);
    }
} 
