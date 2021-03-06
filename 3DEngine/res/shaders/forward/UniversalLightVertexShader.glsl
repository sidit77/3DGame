#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 normal;
layout (location = 3) in vec3 tangent;
layout (location = 4) in vec3 splattingColor;

out vec2 texCoord0;
out vec3 normal0;
out vec3 worldPos0;
out vec3 tangent0;
out vec3 splattingColor0;

uniform mat4 P_model;
uniform mat4 P_MVP;

void main()
{
    gl_Position = P_MVP * vec4(position, 1.0);
    texCoord0 = texCoord;
    splattingColor0 = splattingColor;
    normal0 = (P_model * vec4(normal, 0.0)).xyz;
    worldPos0 = (P_model * vec4(position, 1.0)).xyz;
    tangent0 = (P_model * vec4(tangent, 0.0)).xyz;
}