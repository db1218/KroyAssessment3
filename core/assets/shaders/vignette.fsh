varying vec4 v_color;
varying vec2 v_texCoord0;

uniform vec2 u_resolution;
uniform sampler2D u_sampler2D;
uniform float u_intensity;
uniform float u_innerRadius;
uniform float u_outerRadius;

const float innerRadius = 0.2, outerRadius = 0.5;

void main() {
	vec4 color = texture2D(u_sampler2D, v_texCoord0) * v_color;
	vec2 relativePosition = gl_FragCoord.xy / u_resolution - 0.5;
	float len = length(relativePosition);
	float vignette = smoothstep(outerRadius, innerRadius, len);
	color.rgb = mix(color.rgb, color.rgb * vignette, u_intensity);

	gl_FragColor = color;
}
