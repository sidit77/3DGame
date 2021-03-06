#version 330
#include "forward/lighting.glh"
#include "forward/UserdefinedFragmentEffects.glh"

in vec2 texCoord0;
in vec3 normal0;
in vec3 worldPos0;
in vec3 tangent0;
in vec3 splattingColor0;

uniform PointLight P_pointLight;
uniform float transparency;

void main()
{
    gl_FragColor = baseColor(texCoord0, splattingColor0) * CalcPointLight(P_pointLight, normalize(normal0), worldPos0, texCoord0, tangent0, splattingColor0);
    gl_FragColor.a = transparency;
}