#version 330 core

struct DirLight {
	vec3 direction;
	vec3 colour;
};

struct PointLight {
	vec3 position;
	vec3 colour;
	float constant;
	float linear;
	float quadratic;
};

#define NUM_POINT_LIGHTS 1

in vec2 outTextCoord;

out vec4 FragColour;


uniform sampler2D gPosition;
uniform sampler2D gNormal;
uniform sampler2D gAlbedo;

uniform DirLight dirLight;
uniform PointLight pointLights[NUM_POINT_LIGHTS];

vec3 calcDirLight(DirLight light, vec3 FragPos, vec3 normal);
vec3 calcPointLight(PointLight light, vec3 FragPos, vec3 normal);

void main() {
	vec3 FragPos = texture(gPosition, outTextCoord).rgb;
	vec3 Normal = texture(gNormal, outTextCoord).rgb;
	vec4 Albedo = texture(gAlbedo, outTextCoord);
	
	vec3 result = calcDirLight(dirLight, FragPos, Normal);
	for(int i = 0; i < NUM_POINT_LIGHTS; i++) {
		result += calcPointLight(pointLights[i], FragPos, Normal);
	}
	
	FragColour = vec4(result, 1.0) * Albedo;
}

vec3 calcDirLight(DirLight light, vec3 FragPos, vec3 normal) {
	vec3 dir = normalize(-light.direction);
	
	float ambientStrength = 0.1;
	vec3 ambient = ambientStrength * light.colour;
	
	
	float diffuseFactor = max(dot(normal, dir), 0.0);
	vec3 diffuse = diffuseFactor * light.colour;
	
	return (ambient + diffuse);
}

vec3 calcPointLight(PointLight light, vec3 FragPos, vec3 normal) {
	float distance = length(light.position - FragPos);
	float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
	
	vec3 ambient = light.colour * 0.1 * attenuation;
	
	vec3 direction = normalize(light.position - FragPos);
	float diffuseFactor = max(dot(normal, direction), 0.0);
	vec3 diffuse = diffuseFactor * light.colour;
	diffuse *= attenuation;
	
	return (ambient + diffuse);
}