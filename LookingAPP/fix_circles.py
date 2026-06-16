import os
import re
import math

drawable_dir = r"D:\AndroidTools\AndroidProject\XHSApp\app\src\main\res\drawable"

def circle_to_path(cx, cy, r):
    """Convert a circle at (cx, cy) with radius r to SVG path data."""
    # Two arcs to form a complete circle
    return f"M {cx},{cy - r} A {r},{r} 0 1,1 {cx},{cy + r} A {r},{r} 0 1,1 {cx},{cy - r}"

def fix_circle_element(match):
    """Replace a <circle .../> element with an equivalent <path .../> element."""
    attrs = match.group(1)

    # Extract attributes
    cx_match = re.search(r'android:cx="([^"]*)"', attrs)
    cy_match = re.search(r'android:cy="([^"]*)"', attrs)
    r_match = re.search(r'android:r="([^"]*)"', attrs)

    if not (cx_match and cy_match and r_match):
        return match.group(0)  # Skip if we can't parse

    cx = cx_match.group(1)
    cy = cy_match.group(1)
    r = r_match.group(1)

    # Remove cx, cy, r attributes
    new_attrs = re.sub(r'android:(cx|cy|r)="[^"]*"\s*', '', attrs)
    new_attrs = new_attrs.strip()

    path_data = circle_to_path(float(cx), float(cy), float(r))

    return f'<path android:pathData="{path_data}" {new_attrs}/>'

# Fix all XML files in the drawable directory
for filename in os.listdir(drawable_dir):
    if not filename.endswith('.xml'):
        continue

    filepath = os.path.join(drawable_dir, filename)
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    if '<circle' not in content:
        continue

    # Replace <circle .../> with <path .../>
    new_content = re.sub(r'<circle\s+([^/>]*)\s*/>', fix_circle_element, content)

    if new_content != content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)
        print(f"Fixed: {filename}")

print("All files fixed!")
