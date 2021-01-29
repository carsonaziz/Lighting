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

struct SpotLight {
	vec3 position;
	vec3 direction;
	float cutOff;
	vec3 colour;
	float constant;
	float linear;
	float quadratic;
};

#define NUM_POINT_LIGHTS 1
#define NUM_SPOT_LIGHTS 1

in vec2 outTextCoord;
in vec3 outNormal;
in vec3 FragPos;

out vec4 FragColour;

uniform DirLight dirLight;
uniform PointLight pointLights[NUM_POINT_LIGHTS];
uniform SpotLight spotLights[NUM_SPOT_LIGHTS];

uniform sampler2D meshTexture;

vec3 calcDirLight(DirLight light, vec3 normal);
vec3 calcPointLight(PointLight light, vec3 normal);
vec3 calcSpotLight(SpotLight light, vec3 normal);

void main() {
	vec3 norm = normalize(outNormal);
	
	vec3 result = calcDirLight(dirLight, norm);
	for(int i = 0; i < NUM_POINT_LIGHTS; i++) {
		result += calcPointLight(pointLights[i], norm);
	}
	for(int i = 0; i < NUM_SPOT_LIGHTS; i++) {
		//result += calcSpotLight(spotLights[i], norm);
	}

	FragColour = vec4(result, 1.0) * texture(meshTexture, outTextCoord);
}

vec3 calcDirLight(DirLight light, vec3 normal) {
	vec3 dir = normalize(-light.direction);
	
	float ambientStrength = 0.1;
	vec3 ambient = ambientStrength * light.colour;
	
	
	float diffuseFactor = max(dot(normal, dir), 0.0);
	vec3 diffuse = diffuseFactor * light.colour;
	
	return (ambient + diffuse);
}

vec3 calcPointLight(PointLight light, vec3 normal) {
	float distance = length(light.position - FragPos);
	float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
	
	float ambientStrength = 0.1;
	vec3 ambient = ambientStrength * light.colour;
	ambient *= attenuation;
	
	vec3 direction = light.position - FragPos;
	float diffuseFactor = max(dot(normal, direction), 0.0);
	vec3 diffuse = diffuseFactor * light.colour;
	diffuse *= attenuation;
	
	return (ambient + diffuse);
}

vec3 calcSpotLight(SpotLight light, vec3 normal) {
	vec3 lightDir = normalize(light.position - FragPos);
	float theta = dot(lightDir, normalize(-light.direction));
	
	if(theta > light.cutOff) {
		float ambientStrength = 0.1f;
		vec3 ambient = ambientStrength * light.colour;
		
		float diffuseFactor = max(dot(normal, lightDir), 0.0);
		vec3 diffuse = diffuseFactor * light.colour;
		
		float distance = length(light.position - FragPos);
		float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
		
		diffuse *= attenuation;
		
		return (ambient + diffuse);
	} else {
		return vec3(0, 0, 0);
	}
}