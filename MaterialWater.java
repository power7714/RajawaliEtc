import android.opengl.GLES20;
import rajawali.materials.AMaterial;

public class MaterialWater extends AMaterial {
    private static final float PanUV1XSpeed = 0.0213f;
    private static final float PanUV1YSpeed = 0.0026f;
    private static final float PanUV2XSpeed = -0.0198f;
    private static final float PanUV2YSpeed = -0.0032f;
    protected static final String mFShader = "precision mediump float;\nvarying vec2 vTextureCoord;\nvarying vec4 vPanUV;\nuniform sampler2D uDiffuseTexture;\nuniform sampler2D uLookupTexture;\nuniform vec3 uColTint;\nvoid main() {\n\tvec2 perturbUV = texture2D(uLookupTexture, vPanUV.xy).xy * 2.0 - 1.0;\n\tgl_FragColor = texture2D(uDiffuseTexture, vTextureCoord + (perturbUV * vec2(0.0125,0.0125)));\n\tvec3 target = gl_FragColor.rgb * uColTint.rgb;\n\tgl_FragColor.rgb = target + gl_FragColor.rgb *(gl_FragColor.rgb - target);\n}\n";
    protected static final String mVShader = "uniform mat4 uMVPMatrix;\nuniform vec2 uPanUV1;\nuniform vec2 uPanUV2;\nattribute vec4 aPosition;\nattribute vec2 aTextureCoord;\nvarying vec2 vTextureCoord;\nvarying vec4 vPanUV;\nvoid main() {\n\tgl_Position = uMVPMatrix * aPosition;\n\tvTextureCoord = aTextureCoord;\n\tvPanUV.xy = aTextureCoord.xy * vec2(6.0,6.0) + uPanUV1;\n\tvPanUV.zw = aTextureCoord.yx * vec2(7.35,7.35) + uPanUV2;\n}\n";
    private float PanUV1X;
    private float PanUV1Y;
    private float PanUV2X;
    private float PanUV2Y;
    private float mColTintB;
    private float mColTintG;
    private float mColTintR;
    protected int muColTintHandle;
    protected int muPanUV1Handle;
    protected int muPanUV2Handle;

    public MaterialWater() {
        super(mVShader, mFShader, false);
        setShaders();
    }

    public MaterialWater(String vertexShader, String fragmentShader) {
        super(vertexShader, fragmentShader, false);
        setShaders();
    }

    public void updateParameters(float time) {
        this.PanUV1X = PanUV1XSpeed * time;
        this.PanUV1Y = PanUV1YSpeed * time;
        this.PanUV2X = PanUV2XSpeed * time;
        this.PanUV2Y = PanUV2YSpeed * time;
    }

    public void setShaders(String vertexShader, String fragmentShader) {
        super.setShaders(vertexShader, fragmentShader);
        this.muPanUV1Handle = getUniformLocation("uPanUV1");
        this.muPanUV2Handle = getUniformLocation("uPanUV2");
        this.muColTintHandle = getUniformLocation("uColTint");
    }

    public void useProgram() {
        super.useProgram();
        GLES20.glUniform2f(this.muPanUV1Handle, this.PanUV1X, this.PanUV1Y);
        GLES20.glUniform2f(this.muPanUV2Handle, this.PanUV2X, this.PanUV2Y);
        GLES20.glUniform3f(this.muColTintHandle, this.mColTintR, this.mColTintG, this.mColTintB);
    }

    public void setMatLightColor(float Rin, float Gin, float Bin) {
        this.mColTintR = Rin * 2.0f;
        this.mColTintG = Gin * 2.0f;
        this.mColTintB = Bin * 2.0f;
    }
}
