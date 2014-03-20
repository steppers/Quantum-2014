package old.Scripts;

import old.engine.components.Component;
import old.engine.config.Options;
import old.engine.core.Input;
import old.engine.core.Time;
import old.engine.core.Window;
import old.engine.ext.GameObject;
import old.engine.math.Vector2f;
import old.engine.math.Vector3f;

public class FreeFlyCamera extends Component
{   
    private boolean mouseLocked = false;
    private Vector2f centerPosition = new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2);
    private Vector3f movementVector;
    
    public FreeFlyCamera(GameObject gameObject)
    {
        super(gameObject, "FreeFlyCamera");
    }
    
    @Override
    public void input(){
        if (Input.getKey(Input.KEY_ESCAPE)) {
            Input.setCursor(true);
            mouseLocked = false;
        }
        if (Input.getMouseDown(0)) {
            Input.setMousePosition(centerPosition);
            Input.setCursor(false);
            mouseLocked = true;
        }

        movementVector = Vector3f.ZERO_VECTOR;

        if (Input.getKey(Input.KEY_W)) {
            movementVector = movementVector.add(gameObject.getTransform().forward);
        }
        if (Input.getKey(Input.KEY_S)) {
            movementVector = movementVector.sub(gameObject.getTransform().forward);
        }
        if (Input.getKey(Input.KEY_A)) {
            movementVector = movementVector.sub(gameObject.getTransform().left);
        }
        if (Input.getKey(Input.KEY_D)) {
            movementVector = movementVector.add(gameObject.getTransform().left);
        }

        if (mouseLocked) {
            Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);

            boolean rotY = deltaPos.getX() != 0;
            boolean rotX = deltaPos.getY() != 0;

            if (rotY) {
                gameObject.getTransform().setRotation(gameObject.getTransform().getRotation().getX(),
                                                      gameObject.getTransform().getRotation().getY() - deltaPos.getX() * Options.MOUSE_SENSITIVITY,
                                                      gameObject.getTransform().getRotation().getZ());
            }
            if (rotX) {
                gameObject.getTransform().setRotation(gameObject.getTransform().getRotation().getX() + deltaPos.getY() * Options.MOUSE_SENSITIVITY,
                                                      gameObject.getTransform().getRotation().getY(),
                                                      gameObject.getTransform().getRotation().getZ());
            }
            if (rotY || rotX) {
                Input.setMousePosition(centerPosition);
            }
        }
    }
    
    @Override
    public void update(){
        float movAmt = (float) (Options.FF_MOVE_SPEED * Time.getDelta());
        
        if(Input.getKey(Input.KEY_LSHIFT)){
            movAmt = (float) (Options.FF_MOVE_SPEED * Time.getDelta());
        }
        
        if (movementVector.length() > 0) {
            movementVector = movementVector.normalized();
        }
        gameObject.getTransform().setPos(gameObject.getTransform().getPos().add(movementVector.mul(movAmt)));
    }
    
}