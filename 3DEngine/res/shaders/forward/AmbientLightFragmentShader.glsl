#version 330
#include "forward/UserdefinedFragmentEffects.glh"

in vec2 texCoord0;
in vec3 splattingColor0;

uniform vec3 P_ambient;
uniform float transparency;

void main()
{
	gl_FragColor = baseColor(texCoord0, splattingColor0) * vec4(P_ambient, 1);
    gl_FragColor.a = transparency;
}