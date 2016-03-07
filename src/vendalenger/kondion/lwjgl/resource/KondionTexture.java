/*
 * Copyright 2015 Neal Nicdao
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package vendalenger.kondion.lwjgl.resource;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

import vendalenger.kondion.js.JSDrawable;

public class KondionTexture implements JSDrawable {

	public int width, height;
	private int imageWidth, imageHeight;
	private int minFilter, magFilter;
	private boolean mipmapped;
	private int textureId;
	private int wrapS, wrapT;

	public KondionTexture(int id, int width, int height, int miFilter,
			int maFilter, int awrapS, int awrapT, boolean mipped) {
		textureId = id;
		imageWidth = width;
		imageHeight = height;
		minFilter = miFilter;
		magFilter = maFilter;
		wrapS = awrapS;
		wrapT = awrapT;
		mipmapped = mipped;
		this.width = width;
		this.height = height;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, textureId);
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public int getTextureId() {
		return textureId;
	}

	public int getWrapS() {
		return wrapS;
	}

	public int getWrapT() {
		return wrapT;
	}

	public boolean isMipmap() {
		return mipmapped;
	}

	public static void unBind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
