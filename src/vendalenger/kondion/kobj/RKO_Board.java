package vendalenger.kondion.kobj;

import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMultMatrixf;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import java.io.File;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import vendalenger.kondion.lwjgl.GLDrawing;
import vendalenger.kondion.lwjgl.resource.KondionLoader;
import vendalenger.kondion.lwjgl.resource.KondionShader;
import vendalenger.kondion.objectbase.KObj_Renderable;

public class RKO_Board extends KObj_Renderable {
	
	private FloatBuffer buffer;
	KondionShader eggs;
	
	public RKO_Board() {
		eggs = KondionLoader.loadNashShader(new File("KondionTestGame_0/testshader.nash"));
	}
	
	@Override
	public void render(int type) {
		if (buffer == null)
			buffer = BufferUtils.createFloatBuffer(16);
		
		buffer.clear();
		actTransform.get(buffer);
		
		glPushMatrix();
		glLoadIdentity();
		glMultMatrixf(buffer);
		//eggs.useProgram();
		if (material != null)
			material.bind(type);
		GLDrawing.setCoords(new float[] {1, 1, 0, 1, 0, 0, 1, 0});
		GLDrawing.renderQuad(1, 1, KondionLoader.textures.get("K_Cube"));
		//KondionShader.unbind();
		if (material != null)
			material.unbind();
		glPopMatrix();
	}

	@Override
	public void update() {
		if (this.s != null) {
			this.s.setMember("obj", this);
			if (this.s.containsKey("onupdate")) {
				this.s.callMember("onupdate");
			}
		}
	}

	@Override
	public void updateB() {
		// TODO Auto-generated method stub
		
	}
}
