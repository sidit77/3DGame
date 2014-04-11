#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;
layout (location = 4) in vec3 splattingColor;

out vec2 texCoord0;
out vec3 splattingColor0;

uniform mat4 P_MVP;

void main()
{
    gl_Position = P_MVP * vec4(position, 1.0);
    texCoord0 = texCoord;
    splattingColor0 = splattingColor;
}