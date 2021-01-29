#version 330 core

layout (location = 0) out vec3 gPosition;
layout (location = 1) out vec3 gNormal;
layout (location = 2) out vec4 gAlbedo;

in vec2 outTextCoord;
in vec3 FragPos;
in vec3 outNormal;

uniform sampler2D meshTexture;

void main() {
	gPosition = FragPos;
	gNormal = normalize(outNormal);
	gAlbedo = texture(meshTexture, outTextCoord);
}