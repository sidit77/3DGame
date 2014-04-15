#version 120

in vec2 texCoord0;

uniform sampler2D diffuse;
uniform float brightness;

void main(){
    gl_FragColor = texture2D(diffuse, texCoord0.xy) * vec4(brightness,brightness,brightness,1);
}