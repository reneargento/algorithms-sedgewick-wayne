package util;

// Adapted from the code in https://introcs.cs.princeton.edu/java/15inout/stddraw3d/StdDraw3D.java

/*************************************************************************
 *  Compilation:  javac StdDraw3D.java
 *  Execution:    java StdDraw3D
 *
 *  Standard 3D graphics library.
 *
 *  Remarks
 *  -------
 *   - Increase the static variable NUM_DIVISIONS to make objects appear smoother;
 *  decrease it to improve performance.
 *
 *   - Set the boolean variable waitForShow to true to wait until show() is called before rendering the objects
 *  to the screen; if waitForShow is false, they will all display as soon as they are drawn.
 *
 *   - If waitForShow is set, each call to show() will erase the screen and then draw all the objects entered
 *  since the last show().  This is to avoid flickering for animation.  If you don't want the screen to be erased
 *  each time, do not use waitForShow.
 *
 *   - Zooming in too much will often cause part of the screen to be erased.  There is no fix for this
 *  problem at the moment, but you can avoid it by setting a high initial zoom and then zooming out rather than
 *  zooming in.
 *
 *   - To change the default light setting, modify the addLight() function.
 *
 *************************************************************************/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//3D Classes
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.image.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.ViewingPlatform;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import edu.princeton.cs.algs4.StdOut;

/**
 *  <i>Standard draw 3D</i>. Our class StdDraw3D provides a basic capability for
 *  creating 3D drawings with your programs. It uses a Java3D model to generate
 *  and move spheres, boxes, points and other 3D shapes in a virtual 3D universe.
 *  <p>
 *  This library is still in progress.  Contact Aaron Schneider (<a href="mailto:aschneid@princeton.edu">aschneid@princeton.edu</a>) for more information,
 *  or to report problems.
 */

// To compile this class import all libraries in libs/java3d to the project
public final class StdDraw3D implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
    // Scene groups
    private static SimpleUniverse universe;
    private static BranchGroup rootGroup, lightGroup, shapeGroup1, shapeGroup2;
    private static BranchGroup onscreenGroup, offscreenGroup;

    // Global variables
    private static Canvas3D canvas;
    private static float zoom = 1;
    private static Color penColor;
    private static float penWidth = 1;
    private static boolean spacePressed = false;

    // How smooth to make the objects
    private static final int NUM_DIVISIONS = 100;

    private static final int primitiveflags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;

    // Pre-defined colors
    public static final Color BLACK      = Color.BLACK;
    public static final Color BLUE       = Color.BLUE;
    public static final Color CYAN       = Color.CYAN;
    public static final Color DARK_GRAY  = Color.DARK_GRAY;
    public static final Color GRAY       = Color.GRAY;
    public static final Color GREEN      = Color.GREEN;
    public static final Color LIGHT_GRAY = Color.LIGHT_GRAY;
    public static final Color MAGENTA    = Color.MAGENTA;
    public static final Color ORANGE     = Color.ORANGE;
    public static final Color PINK       = Color.PINK;
    public static final Color RED        = Color.RED;
    public static final Color WHITE      = Color.WHITE;
    public static final Color YELLOW     = Color.YELLOW;

    // Default canvas size is SIZE-by-SIZE-by-SIZE
    private static final int DEFAULT_SIZE = 512;
    private static int width  = DEFAULT_SIZE;
    private static int height = DEFAULT_SIZE;

    private static final float MIN_ZOOM = 1.0f;

    // Boundary of drawing canvas
    private static final double DEFAULT_MIN = 0.0;
    private static final double DEFAULT_MAX = 1.0;
    private static double xMin, yMin, zMin, xMax, yMax, zMax;
    private static double xUserMin, yUserMin, zUserMin, xUserMax, yUserMax, zUserMax;

    // Default font
    private static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 16);

    // Current font
    private static Font font;

    // Wait for show or render immediately?
    private static boolean waitForShow = false;

    // Singleton for callbacks: avoids generation of extra .class files
    private static StdDraw3D stdDraw3D = new StdDraw3D();

    // The frame for drawing to the screen
    private static JFrame frame;

    // Mouse state
    private static boolean mousePressed = false;
    private static double mouseX = 0;
    private static double mouseY = 0;

    // Keyboard state
    private static Character lastKeyTyped = null;

    // Not instantiable
    private StdDraw3D() { }

    // Static initializer
    static { init(); }

    /*****************************************************************
     *                         Access Methods                        *
     *****************************************************************/

    /**
     * Set the window size to width-by-height-by-depth pixels
     *
     * @param width the width as a number of pixels
     * @param height the height as a number of pixels
     */
    public static void setCanvasSize(int width, int height) {
        if (width < 1 || height < 1) {
            throw new RuntimeException("Dimensions must be positive");
        }
        StdDraw3D.width = width;
        StdDraw3D.height = height;
        init();
    }

    /*************************************************************************
     *  User and screen coordinate systems
     *************************************************************************/

    /**
     * Set the default scale for all three dimensions
     */
    public static void setScale() {
        setScale(DEFAULT_MIN, DEFAULT_MAX);
    }

    /**
     * Set the scale for all three dimensions
     * @param min the minimum value of each scale
     * @param max the maximum value of each scale
     */
    public static void setScale(double min, double max) {
        double size = max - min;
        double center = size / 2;

        xMin = center - 0.5;
        xMax = center + 0.5;
        xUserMin = min;
        xUserMax = max;

        yMin = xMin;
        yMax = xMax;
        yUserMin = xUserMin;
        yUserMax = xUserMax;

        zMin = xMin;
        zMax = xMax;
        zUserMin = xUserMin;
        zUserMax = xUserMax;

        setZoom((float) size);
    }

    /**
     * Set the scale for the X (width) dimension
     * @param min the minimum X-value
     * @param max the maximum X-value
     */
    public static void setXscale(double min, double max) {
        double size = max - min;
        double center = size / 2;

        xMin = center - 0.5;
        xMax = center + 0.5;
        xUserMin = min;
        xUserMax = max;
    }

    /**
     * Set the scale for the Y (height) dimension
     * @param min the minimum Y-value
     * @param max the maximum Y-value
     */
    public static void setYscale(double min, double max) {
        double size = max - min;
        double center = size / 2;

        yMin = center - 0.5;
        yMax = center + 0.5;
        yUserMin = min;
        yUserMax = max;
    }

    /**
     * Set the scale for the Z (depth) dimension
     * @param min the minimum Z-value
     * @param max the maximum Z-value
     */
    public static void setZscale(double min, double max) {
        double size = max - min;
        double center = size / 2;

        zMin = center - 0.5;
        zMax = center + 0.5;
        zUserMin = min;
        zUserMax = max;

        setZoom((float) size);
    }

    /**
     * Sets the current pen color (for drawing lines) to color
     */
    public static void setPenColor(Color color) {
        penColor = color;
    }

    /**
     * Sets the current pen width (for drawing lines) to width
     */
    public static void setPenWidth(float width) {
        penWidth = width;
    }

    /**
     * Indicates whether a draw function should display immediately or
     * wait for a call to show().  If it displays immediately (i.e. no
     * offscreen canvas is used), and animation is also desired, it
     * will be important to maintain a reference to the returned
     * Node object and use the moveObject() method to avoid flickering.
     */
    public static void setWaitForShow(boolean value) {
        waitForShow = value;
    }

    /**
     * Scaling methods between user-coordinates and screen-coordinates
     */
    // From user to screen
    private static float scaleX(double coordinate) { return (float)(((((coordinate - xUserMin)/(xUserMax - xUserMin))*(xMax - xMin)) - .5f)*(zoom)); }
    private static float scaleY(double coordinate) { return (float)(((((coordinate - yUserMin)/(yUserMax - yUserMin))*(yMax - yMin)) - .5f)*(zoom)); }
    private static float scaleZ(double coordinate) { return (float)(((((coordinate - zUserMin)/(zUserMax - zUserMin))*(zMax - zMin)) - .5f)*(zoom)); }
    private static float scaleWidth(double coordinate) { return (float)(coordinate*(xMax - xMin)*zoom/(xUserMax - xUserMin)); }
    private static float scaleHeight(double coordinate) { return (float)(coordinate*(yMax - yMin)*zoom/(yUserMax - yUserMin)); }
    private static float scaleDepth(double coordinate) { return (float)(coordinate*(zMax - zMin)*zoom/(zUserMax - zUserMin)); }

    // From screen to user
    private static float userX(double coordinate) { return (float)(((coordinate+0.5f)/(xMax - xMin))*(xUserMax - xUserMin) + xUserMin); }
    private static float userY(double coordinate) { return (float)(((coordinate+0.5f)/(yMax - yMin))*(yUserMax - yUserMin) + yUserMin); }
    private static float userZ(double coordinate) { return (float)(((coordinate+0.5f)/(zMax - zMin))*(zUserMax - zUserMin) + zUserMin); }

    /**
     * @deprecated
     */
    public void actionPerformed(ActionEvent e) {

    }

    /*************************************************************************
     *  Mouse interactions.
     *************************************************************************/

    /**
     * @deprecated
     */
    public static boolean mousePressed() {
        return mousePressed;
    }

    /**
     * Where is the mouse?
     * @return the value of the X coordinate of the mouse
     */
    public static double mouseX() {
        return mouseX;
    }

    /**
     * Where is the mouse?
     * @return the value of the Y coordinate of the mouse
     */
    public static double mouseY() {
        return mouseY;
    }

    /** @deprecated
     */
    public void mouseClicked (MouseEvent e) { }

    /** @deprecated
     */
    public void mouseEntered (MouseEvent e) { }

    /** @deprecated
     */
    public void mouseExited (MouseEvent e) { }

    /**
     * @deprecated
     */
    public void mousePressed (MouseEvent e) {
        mouseX = StdDraw3D.userX(e.getX());
        mouseY = StdDraw3D.userY(e.getY());
        mousePressed = true;
    }

    /**
     * @deprecated
     */
    public void mouseReleased(MouseEvent e) { mousePressed = false; }

    /**
     * @deprecated
     */
    public void mouseDragged(MouseEvent e)  {
        float newX = StdDraw3D.userX(e.getX());
        float newY = StdDraw3D.userY(e.getY());
    }

    /**
     * @deprecated
     */
    public void mouseMoved(MouseEvent e) {
        mouseX = StdDraw3D.userX(e.getX());
        mouseY = StdDraw3D.userY(e.getY());
    }

    /*************************************************************************
     *  Keyboard interactions.
     *************************************************************************/

    /**
     * @deprecated
     */
    public static boolean hasNextKeyTyped() { return lastKeyTyped != null; }

    /**
     * @deprecated
     */
    public static char nextKeyTyped() {
        char c = lastKeyTyped;
        lastKeyTyped = null;
        return c;
    }

    /**
     * @deprecated
     */
    public void keyTyped(KeyEvent e) {
        lastKeyTyped = e.getKeyChar();
    }

    /** Under the current configuration, z will zoom the camera in,
     * while x will zoom it out.  Pressing space will also toggle the
     * global 'spacePressed' variable. This should be changed or overriden
     * if different behavior is desired.
     */
    public void keyPressed (KeyEvent e)   {
        if (e.getKeyChar() == 'z') {
            zoomIn();
        } else if (e.getKeyChar() == 'x') {
            zoomOut();
        } else if (e.getKeyChar() == ' ') {
            spacePressed = !spacePressed;
        }
    }

    /** @deprecated
     */
    public void keyReleased (KeyEvent e) { }


    /*****************************************************************
     *                      Initialization Methods                   *
     *****************************************************************/

    /**
     * Helper init method
     */
    private static void createCanvasPanel(Panel panel) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        panel.setLayout(gridBagLayout);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        GraphicsConfiguration graphicsConfiguration = SimpleUniverse.getPreferredConfiguration();

        canvas = new Canvas3D(graphicsConfiguration);
        canvas.setSize(width, height);
        panel.add(canvas, gridBagConstraints);
    }

    /**
     * Initializes a 3D scene
     */
    public static void init() {
        Panel canvasPanel = new Panel();
        createCanvasPanel(canvasPanel);

        if (frame != null) {
            frame.setVisible(false);
        }
        frame = new JFrame();
        frame.add(canvasPanel);

        canvas.addKeyListener(stdDraw3D);
        canvas.addMouseListener(stdDraw3D);
        canvas.addMouseMotionListener(stdDraw3D);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // closes all windows
        frame.setResizable(false);
        frame.pack();

        rootGroup = new BranchGroup();
        rootGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        rootGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);

        shapeGroup1 = new BranchGroup();
        shapeGroup1.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        shapeGroup1.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        shapeGroup1.setCapability(BranchGroup.ALLOW_DETACH);

        shapeGroup2 = new BranchGroup();
        shapeGroup2.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        shapeGroup2.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        shapeGroup2.setCapability(BranchGroup.ALLOW_DETACH);

        lightGroup = new BranchGroup();
        lightGroup.setCapability(BranchGroup.ALLOW_DETACH);
        lightGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);

        addLight();

        onscreenGroup = shapeGroup1;
        offscreenGroup = shapeGroup2;

        rootGroup.addChild(onscreenGroup);
        rootGroup.addChild(lightGroup);

        universe = new SimpleUniverse(canvas, 2);
        universe.addBranchGraph(rootGroup);

        ViewingPlatform viewingPlatform = universe.getViewingPlatform();
        viewingPlatform.setNominalViewingTransform();
        zoomOut();

        // Add orbit behavior to the ViewingPlatform
        OrbitBehavior orbit = new OrbitBehavior(canvas, OrbitBehavior.REVERSE_ALL ^ OrbitBehavior.STOP_ZOOM);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), width);
        orbit.setMinRadius(0);
        orbit.setSchedulingBounds(bounds);
        viewingPlatform.setViewPlatformBehavior(orbit);

        setPenColor(WHITE);
        setPenWidth(1);

        frame.setVisible(true);
    }

    /*****************************************************************
     *                      Functionality Methods                    *
     *****************************************************************/

    /**
     * Renders the offscreen buffer to the main screen.
     * If waitForShow is set to false, this will have no effect.
     */
    public static void show() {
        if (waitForShow) {
            rootGroup.addChild(offscreenGroup);

            if (offscreenGroup == shapeGroup1) {
                offscreenGroup = shapeGroup2;
                onscreenGroup = shapeGroup1;
            } else {
                offscreenGroup = shapeGroup1;
                onscreenGroup = shapeGroup2;
            }

            rootGroup.removeChild(offscreenGroup);
            offscreenGroup.removeAllChildren();
        }
    }

    /**
     * Clears the screen (and the offscreen memory) of all objects
     */
    public static void clear() {
        onscreenGroup.removeAllChildren();

        if (waitForShow) {
            offscreenGroup.removeAllChildren();
        }
    }

    /**
     * Zooms the camera in logarithmically (i.e. the amount zoomed will decrease as it zooms in)
     */
    public static void zoomIn() {
        zoom -= zoom / 10;

        ViewingPlatform viewingPlatform = universe.getViewingPlatform();
        TransformGroup transformGroup = viewingPlatform.getMultiTransformGroup().getTransformGroup(0);
        Transform3D transform3D = new Transform3D();
        transformGroup.getTransform(transform3D);
        transform3D.setScale(zoom);
        transformGroup.setTransform(transform3D);
    }

    /**
     * Zooms the camera out logarithmically (i.e. the amount zoomed will increase as it zooms out)
     */
    public static void zoomOut() {
        zoom += zoom / 10;

        ViewingPlatform viewingPlatform = universe.getViewingPlatform();
        TransformGroup transformGroup = viewingPlatform.getMultiTransformGroup().getTransformGroup(0);
        Transform3D transform3D = new Transform3D();
        transformGroup.getTransform(transform3D);
        transform3D.setScale(zoom);
        transformGroup.setTransform(transform3D);
    }

    /**
     * Sets the zoom to a particular magnification z
     */
    public static void setZoom (float z) {
        zoom = z;
        ViewingPlatform viewingPlatform = universe.getViewingPlatform();
        TransformGroup transformGroup = viewingPlatform.getMultiTransformGroup().getTransformGroup(0);
        Transform3D transform3D = new Transform3D();
        transformGroup.getTransform(transform3D);
        transform3D.setScale(zoom);
        transformGroup.setTransform(transform3D);
    }

    /**
     * Adds the default lighting to the scene
     * Called automatically by init()
     */
    public static void addLight() {
        addDirectionalLight(-4f, 7f, 12f, WHITE);
        addDirectionalLight(4f, -7f, -12f, WHITE);
    }

    /**
     * Adds a directional light of color col which appears to come from (x, y, z)
     */
    public static void addDirectionalLight(double x, double y, double z, Color col) {

        Color3f lightColor = new Color3f(col);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 10000000.0);
        Vector3f lightDirection = new Vector3f((float) x, (float) y, (float) z);
        DirectionalLight light = new DirectionalLight(lightColor, lightDirection);
        light.setInfluencingBounds(bounds);

        BranchGroup branchGroup = new BranchGroup();
        branchGroup.addChild(light);
        lightGroup.addChild(branchGroup);
    }

    /*****************************************************************
     *                          Drawing Methods                      *
     *****************************************************************/

    /**
     * Creates a default appearance based on the current penColor and
     * penWidth. This is used in conjunction with the draw methods to
     * paint new objects.
     */
    private static Appearance createAppearance() {
        Appearance appearance = new Appearance();

        Color3f color = new Color3f(penColor);
        Color3f black = new Color3f(0, 0, 0);
        appearance.setMaterial(new Material(color, black, color, black, 1));

        return appearance;
    }

    /**
     * Draws a custom Java3D shape. This shape needs to be defined and instantiated elsewhere.
     * Uses penColor for its color under normal white light
     * Returns a Node representing the shape object, which
     * can be passed to the moveObject method to move it once it has been drawn.
     * @param shape the object to be drawn
     * @param x the x coordinate of the center of the object
     * @param y the y coordinate of the center of the object
     * @param z the z coordinate of the center of the object
     */
    public static TransformGroup drawShape (Shape3D shape, double x, double y, double z) {
        Appearance appearance = createAppearance();
        shape.setAppearance(appearance);

        TransformGroup transformGroup = new TransformGroup();
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D transform3D = new Transform3D();

        Vector3f vector = new Vector3f(scaleX(x), scaleY(y), scaleZ(z));
        transform3D.setTranslation(vector);
        transformGroup.setTransform(transform3D);
        transformGroup.addChild(shape);

        BranchGroup branchGroup = new BranchGroup();
        branchGroup.setCapability(BranchGroup.ALLOW_DETACH);
        branchGroup.addChild(transformGroup);
        if (waitForShow) {
            offscreenGroup.addChild(branchGroup);
        } else {
            onscreenGroup.addChild(branchGroup);
        }

        return transformGroup;
    }

    /**
     * Draws a Java3D primitive (Sphere, Box, Cone, Cylinder) at (x, y, z).
     * Returns a Node representing the object which can then be passed
     * to the moveObject or loadImage methods.
     * @param shape the previously constructed primitive to be drawn
     * @param x the x coordinate of the center of the box
     * @param y the y coordinate of the center of the box
     * @param z the z coordinate of the center of the box
     */
    private static TransformGroup drawPrimitive (Primitive shape, double x, double y, double z) {
        TransformGroup transformGroup = new TransformGroup();
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D transform3D = new Transform3D();

        Vector3f vector = new Vector3f(scaleX(x), scaleY(y), scaleZ(z));
        transform3D.setTranslation(vector);
        transformGroup.setTransform(transform3D);
        transformGroup.addChild(shape);

        BranchGroup branchGroup = new BranchGroup();
        branchGroup.setCapability(BranchGroup.ALLOW_DETACH);
        branchGroup.addChild(transformGroup);
        if (waitForShow) {
            offscreenGroup.addChild(branchGroup);
        } else {
            onscreenGroup.addChild(branchGroup);
        }

        return transformGroup;
    }

    /**
     * Draws a sphere centered at (x, y, z).
     * To set its color, call setPenColor before this method.
     * If penColor has an alpha component, the sphere will be semi-transparent.
     * Returns a TransformGroup representing the sphere which can then be
     * passed to the moveObject or loadImage methods.
     * @param x the x coordinate of the center of the sphere
     * @param y the y coordinate of the center of the sphere
     * @param z the z coordinate of the center of the sphere
     * @param r the radius of the sphere
     */
    public static TransformGroup drawSphere (double x, double y, double z, double r) {
        Appearance appearance = createAppearance();
        Sphere sphere = new Sphere(scaleDepth(r), primitiveflags, NUM_DIVISIONS, appearance);
        return drawPrimitive(sphere, x, y, z);
    }

    /**
     * Draws a solid sphere centered at (x, y, z) with a 2d image
     * loaded from imageURL and pasted on the surface of the sphere.
     */
    public static TransformGroup drawSphere (double x, double y, double z, double r, String imageURL) {
        Appearance appearance = createAppearance();

        // Load an image from the file
        TextureLoader loader;
        try {
            loader = new TextureLoader(imageURL, "RGBA", new Container());
        } catch (Exception e) {
            throw new RuntimeException ("Could not read from the file '"+ imageURL +"'");
        }
        Texture texture = loader.getTexture();
        texture.setBoundaryModeS(Texture.WRAP);
        texture.setBoundaryModeT(Texture.WRAP);
        texture.setBoundaryColor( new Color4f( 0.0f, 1.0f, 0.0f, 0.0f ) );

        // Set up the texture attributes
        TextureAttributes textureAttributes = new TextureAttributes();
        textureAttributes.setTextureMode(TextureAttributes.REPLACE);

        appearance.setTexture(texture);
        appearance.setTextureAttributes(textureAttributes);

        Sphere sphere = new Sphere(scaleDepth(r), primitiveflags, NUM_DIVISIONS);
        sphere.setAppearance(appearance);
        TransformGroup transformGroup = drawPrimitive(sphere, x, y, z);
        return transformGroup;
    }

    /**
     * Draws a solid rectangular prism centered at (x, y, z).
     * To set its color, call setPenColor before this method.
     * If penColor has an alpha component, the box will be semi-transparent.
     * Returns a TransformGroup representing the box which can then be
     * passed to the moveObject or rotateObject methods.
     * @param x the x coordinate of the center of the box
     * @param y the y coordinate of the center of the box
     * @param z the z coordinate of the center of the box
     * @param w the width (x-direction) of the box
     * @param h the height (y-direction) of the box
     * @param d the depth (z-direction) of the box
     */
    public static TransformGroup drawBox (double x, double y, double z, double w, double h, double d) {
        Appearance appearance = createAppearance();
        Box box = new Box(scaleWidth(w), scaleHeight(h), scaleDepth(d), primitiveflags, appearance, NUM_DIVISIONS);
        return drawPrimitive(box, x, y, z);
    }

    /**
     * Draws a solid rectangular prism centered at (x, y, z) with a 2d image
     * loaded from imageURL and pasted on the surface of the box.
     */
    public static TransformGroup drawBox (double x, double y, double z, double w, double h, double d, String imageURL) {
        Appearance appearance = createAppearance();

        // Load an image from the file
        TextureLoader loader;
        try {
            loader = new TextureLoader(imageURL, "RGBA", new Container());
        } catch (Exception e) {
            throw new RuntimeException ("Could not read from the file '" + imageURL + "'");
        }
        Texture texture = loader.getTexture();
        texture.setBoundaryModeS(Texture.WRAP);
        texture.setBoundaryModeT(Texture.WRAP);
        texture.setBoundaryColor( new Color4f( 0.0f, 1.0f, 0.0f, 0.0f ) );

        // Set up the texture attributes
        TextureAttributes textureAttributes = new TextureAttributes();
        textureAttributes.setTextureMode(TextureAttributes.REPLACE);

        appearance.setTexture(texture);
        appearance.setTextureAttributes(textureAttributes);
        Box box = new Box(scaleWidth(w), scaleHeight(h), scaleDepth(d), primitiveflags, appearance, NUM_DIVISIONS);
        return drawPrimitive(box, x, y, z);
    }

    /**
     * Draws a solid cylinder centered at (x, y, z).
     * To set its color, call setPenColor before this method.
     * If penColor has an alpha component, the cylinder will be semi-transparent.
     * Returns a TransformGroup representing the cylinder which can then be
     * passed to the moveObject or rotateObject methods.
     * @param x the x coordinate of the center of the cylinder
     * @param y the y coordinate of the center of the cylinder
     * @param z the z coordinate of the center of the cylinder
     * @param r the radius of the cylinder
     * @param h the height of the cylinder
     */
    private static TransformGroup drawCylinder (double x, double y, double z, double r, double h) {
        Appearance appearance = createAppearance();
        Cylinder cylinder = new Cylinder (scaleWidth(r), scaleHeight(h), primitiveflags, NUM_DIVISIONS, NUM_DIVISIONS,
                appearance);
        return drawPrimitive(cylinder, x, y, z);
    }

    /**
     * Draws a solid cylinder centered at (x, y, z) with a 2d image
     * loaded from imageURL and pasted on the surface of the cylinder.
     */
    public static TransformGroup drawCylinder (double x, double y, double z, double r, double h, String imageURL) {
        Appearance appearance = createAppearance();

        // Load an image from the file
        TextureLoader loader;
        try {
            loader = new TextureLoader(imageURL, "RGBA", new Container());
        } catch (Exception e) {
            throw new RuntimeException ("Could not read from the file '" + imageURL + "'");
        }
        Texture texture = loader.getTexture();
        texture.setBoundaryModeS(Texture.WRAP);
        texture.setBoundaryModeT(Texture.WRAP);
        texture.setBoundaryColor( new Color4f( 0.0f, 1.0f, 0.0f, 0.0f ) );

        // Set up the texture attributes
        TextureAttributes textureAttributes = new TextureAttributes();
        textureAttributes.setTextureMode(TextureAttributes.REPLACE);

        appearance.setTexture(texture);
        appearance.setTextureAttributes(textureAttributes);
        Cylinder cylinder = new Cylinder (scaleWidth(r), scaleHeight(h), primitiveflags, NUM_DIVISIONS, NUM_DIVISIONS,
                appearance);
        return drawPrimitive(cylinder, x, y, z);
    }

    /**
     * Draws a solid Cone centered at (x, y, z).
     * To set its color, call setPenColor before this method.
     * If penColor has an alpha component, the cone will be semi-transparent.
     * Returns a TransformGroup representing the cone which can then be
     * passed to the moveObject or rotateObject methods.
     * @param x the x coordinate of the center of the cone
     * @param y the y coordinate of the center of the cone
     * @param z the z coordinate of the center of the cone
     * @param r the radius of the cone at its base
     * @param h the height of the cone
     */
    private static TransformGroup drawCone (double x, double y, double z, double r, double h) {
        Appearance appearance = createAppearance();
        Cone cone = new Cone (scaleWidth(r), scaleHeight(h), primitiveflags, NUM_DIVISIONS, NUM_DIVISIONS, appearance);
        return drawPrimitive(cone, x, y, z);
    }

    /**
     * Draws a solid cone centered at (x, y, z) with a 2d image
     * loaded from imageURL and pasted on the surface of the cone.
     */
    public static TransformGroup drawCone (double x, double y, double z, double r, double h, String imageURL) {
        Appearance appearance = createAppearance();

        // Load an image from the file
        TextureLoader loader;
        try {
            loader = new TextureLoader(imageURL, "RGBA", new Container());
        } catch (Exception e) {
            throw new RuntimeException ("Could not read from the file '" + imageURL + "'");
        }
        Texture texture = loader.getTexture();
        texture.setBoundaryModeS(Texture.WRAP);
        texture.setBoundaryModeT(Texture.WRAP);
        texture.setBoundaryColor( new Color4f( 0.0f, 1.0f, 0.0f, 0.0f ) );

        // Set up the texture attributes
        TextureAttributes textureAttributes = new TextureAttributes();
        textureAttributes.setTextureMode(TextureAttributes.REPLACE);

        appearance.setTexture(texture);
        appearance.setTextureAttributes(textureAttributes);
        Cone cone = new Cone (scaleWidth(r), scaleHeight(h), primitiveflags, NUM_DIVISIONS, NUM_DIVISIONS, appearance);
        return drawPrimitive(cone, x, y, z);
    }

    /**
     * Draws points in 3-space as specified by the coordinates x = points[i][0], y = points[i][1], z = points[i][2]
     * @param points an array of the points to be drawn
     */
    public static void drawPoints(double[][] points) {
        int size = points.length;
        Point3f[] coordinates = new Point3f[size];

        for (int i = 0; i < size; i++) {
            coordinates[i] = new Point3f(scaleX(points[i][0]), scaleY(points[i][1]), scaleZ(points[i][2]));
        }

        GeometryArray geometryArray = new PointArray(size, PointArray.COORDINATES);
        geometryArray.setCoordinates(0, coordinates);

        BranchGroup branchGroup = new BranchGroup();
        branchGroup.setCapability(BranchGroup.ALLOW_DETACH);
        branchGroup.addChild(new Shape3D(geometryArray));

        if (waitForShow) {
            offscreenGroup.addChild(branchGroup);
        } else {
            onscreenGroup.addChild(branchGroup);
        }
    }

    /**
     * Draws triangles which are defined by x1 = points[i][0], y1 = points[i][1], z1 = points[i][2],
     * x2 = points[i][3], etc.
     * All of the points will be the width and color specified by the setPenColor and setPenWidth methods.
     * @param points an array of the points to be connected.  The first dimension is
     * unspecified, but the second dimension should be 9 (the 3-space coordinates of each vertex)
     */
    public static Shape3D drawTriangles (double[][] points) {
        int size = points.length;
        Point3f[] coordinates = new Point3f[size * 3];

        for (int i = 0; i < size; i++) {
            coordinates[3 * i] = new Point3f(scaleX(points[i][0]), scaleY(points[i][1]), scaleZ(points[i][2]));
            coordinates[3 * i + 1] = new Point3f(scaleX(points[i][3]), scaleY(points[i][4]), scaleZ(points[i][5]));
            coordinates[3 * i + 2] = new Point3f(scaleX(points[i][6]), scaleY(points[i][7]), scaleZ(points[i][8]));
        }

        GeometryArray geometryArray = new TriangleArray(size * 3, LineArray.COORDINATES);
        geometryArray.setCoordinates(0, coordinates);
        Shape3D shape = new Shape3D(geometryArray);
        StdDraw3D.drawShape(shape, 0, 0, 0);
        return shape;
    }

    /**
     * Connects points in 3-space as specified by the coordinates x = points[i][0], y = points[i][1], z = points[i][2]
     * All of the points will be the width and color specified by the setPenColor and setPenWidth methods.
     * The points are connected in the order given by the array,
     * i.e. points[0]--points[1], points[2]--points[3], etc.
     * @param points an array of the points to be connected.  The first dimension is
     * unspecified, but the second dimension should be 3 (the 3-space coordinates)
     */
    public static Shape3D drawLines(double[][] points) {
        int size = points.length;
        Point3f[] coordinates = new Point3f[size];

        for (int i = 0; i < size; i++) {
            coordinates[i] = new Point3f(scaleX(points[i][0]), scaleY(points[i][1]), scaleZ(points[i][2]));
        }

        GeometryArray geometryArray = new LineArray(size, LineArray.COORDINATES);
        geometryArray.setCoordinates(0, coordinates);
        Shape3D shape = new Shape3D(geometryArray);
        Appearance appearance = new Appearance();

        LineAttributes lineAttributes = new LineAttributes();
        lineAttributes.setLineWidth(penWidth);

        PolygonAttributes polygonAttributes = new PolygonAttributes();
        polygonAttributes.setPolygonMode(PolygonAttributes.POLYGON_FILL);

        Color3f color = new Color3f(penColor);
        ColoringAttributes coloringAttributes = new ColoringAttributes();
        coloringAttributes.setShadeModel(ColoringAttributes.SHADE_GOURAUD);
        coloringAttributes.setColor(color);

        appearance.setLineAttributes(lineAttributes);
        appearance.setPolygonAttributes(polygonAttributes);
        appearance.setColoringAttributes(coloringAttributes);

        shape.setAppearance(appearance);

        BranchGroup branchGroup = new BranchGroup();
        branchGroup.setCapability(BranchGroup.ALLOW_DETACH);
        branchGroup.addChild(shape);

        if (waitForShow) {
            offscreenGroup.addChild(branchGroup);
        } else {
            onscreenGroup.addChild(branchGroup);
        }
        return shape;
    }

    /**
     * Draws a single line between (x1, y1, z1) and (x2, y2, z2) with a
     * specified color and width
     */
    private static void drawLine (double x1, double y1, double z1, double x2, double y2, double z2, Color color, float width) {
        Point3f coordinates1 = new Point3f(scaleX(x1), scaleY(y1), scaleZ(z1));
        Point3f coordinates2 = new Point3f(scaleX(x2), scaleY(y2), scaleZ(z2));

        GeometryArray geometryArray = new LineArray(2, LineArray.COORDINATES);
        geometryArray.setCoordinate(0, coordinates1);
        geometryArray.setCoordinate(1, coordinates2);
        Shape3D shape = new Shape3D(geometryArray);
        Appearance appearance = new Appearance();

        LineAttributes lineAttributes = new LineAttributes();
        lineAttributes.setLineWidth(width);

        PolygonAttributes polygonAttributes = new PolygonAttributes();
        polygonAttributes.setPolygonMode(PolygonAttributes.POLYGON_FILL);

        Color3f color3f = new Color3f(color);
        ColoringAttributes coloringAttributes = new ColoringAttributes();
        coloringAttributes.setShadeModel(ColoringAttributes.SHADE_GOURAUD);
        coloringAttributes.setColor(color3f);

        appearance.setLineAttributes(lineAttributes);
        appearance.setPolygonAttributes(polygonAttributes);
        appearance.setColoringAttributes(coloringAttributes);

        shape.setAppearance(appearance);

        BranchGroup branchGroup = new BranchGroup();
        branchGroup.setCapability(BranchGroup.ALLOW_DETACH);
        branchGroup.addChild(shape);

        if (waitForShow) {
            offscreenGroup.addChild(branchGroup);
        } else {
            onscreenGroup.addChild(branchGroup);
        }
    }

    /**
     * Draws a single line, as in the previous method,
     * but uses the default color and width
     */
    public static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2) {
        drawLine(x1, y1, z1, x2, y2, z2, penColor, penWidth);
    }


    /*****************************************************************
     *                     Transformation Methods                    *
     *****************************************************************/

    /**
     * Moves a previously drawn object to a new location
     * @param transformGroup the TransformGroup returned by one of the draw functions
     * @param x the x coordinate of the new location
     * @param y the y coordinate of the new location
     * @param z the z coordinate of the new location
     */
    public static void moveObject (TransformGroup transformGroup, double x, double y, double z) {
        Transform3D transform3D = new Transform3D();
        Vector3f vector3f = new Vector3f((float)x, (float)y, (float)z);
        transform3D.setTranslation(vector3f);
        transformGroup.setTransform(transform3D);
    }

    /**
     * Rotates a previously drawn object around a 3-dimensional vector
     * @param transformGroup the TransformGroup returned by one of the draw functions
     * @param x the x coordinate of the vector to rotate around
     * @param y the y coordinate of the vector to rotate around
     * @param z the z coordinate of the vector to rotate around
     * @param angle how far to rotate (in radians)
     */
    public static void rotateObject (TransformGroup transformGroup, double x, double y, double z, double angle) {
        AxisAngle4d axisAngle4d = new AxisAngle4d(x, y, z, angle);
        Transform3D transform3D = new Transform3D();
        transformGroup.getTransform(transform3D);
        transform3D.setRotation(axisAngle4d);
        transformGroup.setTransform(transform3D);
    }

    /**
     * Utility method which puts the current thread to sleep
     * @param time the time to pause in milliseconds
     */
    public static void pause(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            StdOut.println("Error sleeping");
        }
    }

    /** @deprecated
     * Test client
     */
    public static void main(String[] args) {
        StdDraw3D.setWaitForShow(false);
        StdDraw3D.setXscale(-50,50);
        StdDraw3D.setYscale(-50,50);
        StdDraw3D.setZscale(-50,50);
        StdDraw3D.setPenColor(RED);
        TransformGroup transformGroup = StdDraw3D.drawSphere(0,50,0,20,"earth.jpg");

        double angle = 0;

        while (true) {
            if (spacePressed) {
                StdDraw3D.rotateObject(transformGroup,.3,.4,1, angle += .1);
            }
            StdDraw3D.pause(10);
        }
    }
}