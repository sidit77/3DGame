#version 330
#include "forward/lighting.glh"
#include "forward/UserdefinedFragmentEffects.glh"

in vec2 texCoord0;
in vec3 normal0;
in vec3 worldPos0;

uniform DirectionalLight P_directionalLight;

void main()
{
    gl_FragColor = baseColor(texCoord0) * CalcDirectionalLight(P_directionalLight, normalize(normal0), worldPos0, texCoord0);
}