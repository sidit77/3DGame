#version 330

in vec2 f_texcoord;

uniform sampler2D fbo_texture;
uniform float offset;

void main(void) {
  vec2 texcoord = f_texcoord;
  texcoord.x += sin(texcoord.y * 4*2*3.14159 + offset) / 100;
  gl_FragColor = texture2D(fbo_texture, texcoord);
}