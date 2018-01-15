#version 330 core

layout(location = 0) in vec2 position;
layout(location = 1) in vec2 texcoord;

out vec4 vertexColor;
out vec2 textureCoord;

uniform vec4 color = vec4(0.376, 0.0, 0.067, 1.0);

uniform mat4 model = mat4(1.0);
uniform mat4 view = mat4(1.0);
uniform mat4 projection = mat4(1.0);

uniform mat4 texMatrix = mat4(1.0);

void main()
{
	vertexColor = color;
	textureCoord = (texMatrix * vec4(texcoord, 0.0, 1.0)).xy;
	mat4 mvp = projection * view * model;
    gl_Position = mvp * vec4(position, 0.0, 1.0);
}