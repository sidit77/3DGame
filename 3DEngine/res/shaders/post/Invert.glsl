#version 330

in vec2 f_texcoord;

uniform sampler2D fbo_texture;
uniform float InverseAmount;

void main(){
    vec4 diffuseColor = texture2D(fbo_texture, f_texcoord);
    vec4 invertColor = 1.0 - diffuseColor;
    vec4 outColor = mix(diffuseColor, invertColor, InverseAmount);
    gl_FragColor = vec4(outColor.rgb, diffuseColor.a);
}