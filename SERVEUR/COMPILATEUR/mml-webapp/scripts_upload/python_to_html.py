f = open("tri_image.py", "r")

code = f.read()
print(code)


from pygments import highlight
from pygments.lexers import PythonLexer
from pygments.formatters import HtmlFormatter


