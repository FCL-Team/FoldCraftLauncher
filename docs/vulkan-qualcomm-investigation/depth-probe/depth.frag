#version 450
layout(push_constant) uniform Params { vec4 draw; } pc;
layout(location=0) out vec4 color;
void main() {
    color = pc.draw.z > 0.5 ? vec4(1,0,0,1) : vec4(0,1,0,1);
}
