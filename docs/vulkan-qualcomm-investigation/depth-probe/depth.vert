#version 450
layout(push_constant) uniform Params { vec4 draw; } pc;
void main() {
    vec2 p = vec2((gl_VertexIndex << 1) & 2, gl_VertexIndex & 2) * 2.0 - 1.0;
    gl_Position = vec4(p, pc.draw.x + pc.draw.y * p.x, 1.0);
}
