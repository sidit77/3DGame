#version 330

in vec2 f_texcoord;

uniform sampler2D fbo_texture;
 
void main(void) {
    gl_FragColor = texture2D(fbo_texture, f_texcoord);
}