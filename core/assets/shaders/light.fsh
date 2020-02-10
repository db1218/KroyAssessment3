varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;

void main() {
	vec4 color = texture2D(u_sampler2D, v_texCoord0) * v_color;
//	color.rgb = lerp(color.rgb, vec3(1.0, 1.0, 1.0), 0.2);
//	gl_FragColor = color;
	gl_FragColor = vec4( vec3(color.r+color.g+color.b)/3.0, color.a);;
}
