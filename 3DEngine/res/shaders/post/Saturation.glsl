#version 330

in vec2 f_texcoord;

uniform sampler2D fbo_texture;
uniform float saturation;
 
void main(void) {
    gl_FragColor = texture2D(fbo_texture, f_texcoord)*vec4(saturation, saturation, saturation, 1);
}