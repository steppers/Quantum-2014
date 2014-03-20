package old.engine.components;

import old.engine.ext.GameObject;
import old.engine.ext.SceneManager;
import old.engine.graphics.Material;
import old.engine.math.Matrix4f;

public class MeshRenderer extends Component
{
    private MeshFilter meshFilter;
    private Material material;
    
    public MeshRenderer(GameObject gameObject)
    {
        super(gameObject, "MeshRenderer");
        if(gameObject.getComponent(MeshFilter.class) != null) {
            meshFilter = (MeshFilter) gameObject.getComponent(MeshFilter.class);
        }else{
            meshFilter = new MeshFilter(gameObject);
            gameObject.AddComponent(meshFilter);
        }
    }
    
    @Override
    public void render(){
        material.getShader().bind();
        
        getGameObject().getTransform().calcTransformation();
        Matrix4f transformation = getGameObject().getTransform().getTransformation();
        material.getShader().updateUniforms(transformation,
                        SceneManager.FindGameObjectWithTag("Camera").getComponent(Camera.class).getProjectedTransformation(transformation),
                        SceneManager.FindGameObjectWithTag("Camera").getComponent(Camera.class).getViewMatrix(transformation),
                        SceneManager.FindGameObjectWithTag("Camera").getComponent(Camera.class).getNormalViewMatrix(transformation),
                        material);
        meshFilter.getSharedMesh().draw();
    }  

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}