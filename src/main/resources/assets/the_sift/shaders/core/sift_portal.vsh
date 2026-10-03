#version 150

#moj_import <fog.glsl>

in vec3 Position;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform int FogShape;

out vec4 texProj0;
out vec3 viewPosition;
out float vertexDistance;

vec4 siftProjectionFromPosition(vec4 position) {
    vec4 projection = position * 0.5;
    projection.xy = vec2(projection.x + projection.w, projection.y + projection.w);
    projection.zw = position.zw;
    return projection;
}

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    viewPosition = (ModelViewMat * vec4(Position, 1.0)).xyz;
    texProj0 = siftProjectionFromPosition(gl_Position);
    vertexDistance = fog_distance(ModelViewMat, Position, FogShape);
}
