package vendalenger.kondion.materials;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform4f;

import org.lwjgl.opengl.GL11;

import vendalenger.kondion.Kondion;
import vendalenger.kondion.lwjgl.resource.KLoader;
import vendalenger.kondion.lwjgl.resource.KShader;
import vendalenger.kondion.lwjgl.resource.KTexture;

/**
 * 
 * 
 */
public class KMat_Monotexture implements KMat_erial  {

	public int alphaBlend = 0;
	public KTexture texture;
	private KShader shader;
	private float r, g, b, a;
	private int uni_type, uni_fog, uni_color;
	private boolean alpha;
	
	public KMat_Monotexture() {
		setColorf(1.0f, 1.0f, 1.0f);
		a = 1.0f;
	}
	
	public KMat_Monotexture(String nom) {
		this();
		texture = Kondion.kjs.texture(nom);
	}
	
	/**
	 * Set the colo(u)r, from 0.0f - 1.0f
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setColorf(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	/**
	 * Set the colo(u)r, from 0 - 255
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setColori(int r, int g, int b) {
		this.r = ((float) r) / 255;
		this.g = ((float) g) / 255;
		this.b = ((float) b) / 255;
	}
	
	/**
	 * Set the alpha, 0.0f - 1.0f
	 * @param a
	 */
	public void setAlphaf(float a) {
		alpha = true;
		this.a = a;
	}
	
	public void disableAlpha() {
		alpha = false;
	}
	
	public float getR() {
		return r;
	}
	
	public float getG() {
		return r;
	}
	
	public float getB() {
		return r;
	}
	
	public float getA() {
		return a;
	}

	@Override
	public int bind(int type) {
		if (shader == null) {
			shader = KLoader.shaders.get("K_Monotexture");
			uni_type = shader.uniformLocation("type");
			uni_color = shader.uniformLocation("color");
			uni_fog = shader.uniformLocation("fog");
		}
		
		if (texture != null)
			texture.bind();
		else
			KLoader.getMissingTexture().bind();
		shader.useProgram();
		//KondionShader.uniform1i(eggs, (int) Kondion.getFrame());
		glUniform4f(uni_color, r, g, b, a);
		glUniform1f(uni_fog, Kondion.getWorld().fogIntensity);
		glUniform1i(uni_type, type);
		//glColor4f(r, g, b, a);
		switch (alphaBlend) {
			case 1:
				glBlendFunc(GL_SRC_ALPHA, GL_ONE);
				glDepthMask(false);
				//GL11.glDisable(GL11.GL_DEPTH_TEST);
				break;

			default:
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				break;
		}
		
		return 0;
	}

	@Override
	public int prepare() {
		
		return 0;
	}

	@Override
	public int unbind() {
		shader.unbind();
		glDepthMask(true);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		return 0;
	}

	@Override
	public void fogOverride(float fog) {
		glUniform1f(uni_fog, fog);
	}
}
