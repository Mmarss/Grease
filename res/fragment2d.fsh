#version 330 core

in vec4 vertexColor;
in vec2 textureCoord;

out vec4 fragColor;

uniform bool useTexture;
uniform sampler2D texImage;

void main()
{
	if (useTexture) {
		vec4 textureColor = texture(texImage, textureCoord);
		fragColor = vertexColor * textureColor;
	} else {
		fragColor = vertexColor;
	}
}