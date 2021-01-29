package shaders;

import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import utilities.Utils;

public class GBuffer {
	private int gBufferID;
	
	private int gPositionID;
	private int gNormalID;
	private int gAlbedoID;
	
	public GBuffer(int width, int height) {
		gBufferID = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, gBufferID);

		gPositionID = createBuffer(width, height, GL_COLOR_ATTACHMENT0);
		gNormalID = createBuffer(width, height, GL_COLOR_ATTACHMENT1);
		gAlbedoID = createBuffer(width, height, GL_COLOR_ATTACHMENT2);
		
		int attachments[] = {GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1, GL_COLOR_ATTACHMENT2};
		IntBuffer buffer = Utils.storeDataInIntBuffer(attachments);
		glDrawBuffers(buffer);
		
		createDepthBuffer(width, height);
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	private int createBuffer(int width, int height, int colorAttachment) {
		int bufferID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, bufferID);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA16F, width, height, 0, GL_RGBA, GL_FLOAT, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glFramebufferTexture2D(GL_FRAMEBUFFER, colorAttachment, GL_TEXTURE_2D, bufferID, 0);
		
		return bufferID;
	}
	
	private void createDepthBuffer(int width ,int height) {
		int rboID = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, rboID);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
	        System.out.println("Framebuffer not complete");
		}
	}
	
	public int getGBufferID() {
		return gBufferID;
	}
	
	public int getGPositionID() {
		return gPositionID;
	}
	
	public int getGNormalID() {
		return gNormalID;
	}
	
	public int getGAlbedoID() {
		return gAlbedoID;
	}
}
