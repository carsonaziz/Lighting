#version 330 core

layout (location = 0) in vec3 inPos;
layout (location = 1) in vec2 inTextCoord;
layout (location = 2) in vec3 inNormal;

out vec2 outTextCoord;
out vec3 outNormal;
out vec3 FragPos;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform mat4 root;

void main() {
	FragPos = vec3(model * root * vec4(inPos, 1.0));
	outTextCoord = inTextCoord;
	outNormal = vec3(model * root * vec4(inNormal, 0.0));
	
	gl_Position =  projection * view * model * root * vec4(inPos, 1.0);
}