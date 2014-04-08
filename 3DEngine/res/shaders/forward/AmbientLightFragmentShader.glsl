#version 330
#include "forward/UserdefinedFragmentEffects.glh"

in vec2 texCoord0;

uniform vec3 P_ambient;

void main()
{
	gl_FragColor = baseColor(texCoord0) * vec4(P_ambient, 1);
}