#ifdef GL_ES\n
	precision mediump float; 
#endif

varying vec2 v_texCoords; 
uniform sampler2D u_texture;

uniform float time;


void main()                                   
{
	float speed=3;
	float frequency=100.0;
	float amplitude=0.003;
	float distortion=sin(v_texCoords.y * frequency + (time * speed)) * amplitude;
	gl_FragColor =  texture2D(u_texture, vec2(v_texCoords.x + distortion, v_texCoords.y));
}