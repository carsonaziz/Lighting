#version 330 core

out vec4 FragColour;

uniform vec3 lightColour;

void main() {
	FragColour = vec4(lightColour, 1.0);
}