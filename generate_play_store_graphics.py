import os
from PIL import Image, ImageDraw, ImageFont, ImageFilter

output_dir = "play_store_graphics"
os.makedirs(output_dir, exist_ok=True)

logo_standalone_path = "app/src/main/res/drawable-nodpi/neuro_logo_n_standalone.png"
logo_img = Image.open(logo_standalone_path).convert("RGBA")

# -------------------------------------------------------------
# 1. 512x512 Play Store App Icon (Full Square, Google masks it)
# -------------------------------------------------------------
icon_canvas = Image.new("RGBA", (512, 512), (5, 8, 17, 255)) # Afterglow Canvas #050811

# Subtle background glow behind logo
glow_circle = Image.new("RGBA", (512, 512), (0, 0, 0, 0))
glow_draw = ImageDraw.Draw(glow_circle)
glow_draw.ellipse([80, 80, 432, 432], fill=(0, 229, 255, 45))
glow_draw.ellipse([120, 120, 392, 372], fill=(168, 85, 247, 55))
glow_circle = glow_circle.filter(ImageFilter.GaussianBlur(35))
icon_canvas = Image.alpha_composite(icon_canvas, glow_circle)

# Resize & Center Logo
resized_logo = logo_img.resize((420, 420), Image.Resampling.LANCZOS)
icon_canvas.paste(resized_logo, (46, 46), resized_logo)

icon_canvas.save(os.path.join(output_dir, "01_Play_Store_Icon_512x512.png"))
print("Generated: 01_Play_Store_Icon_512x512.png")

# -------------------------------------------------------------
# 2. 1024x500 Feature Graphic
# -------------------------------------------------------------
fg_canvas = Image.new("RGBA", (1024, 500), (5, 8, 17, 255))

# Background Neural Circuit Glow
fg_glow = Image.new("RGBA", (1024, 500), (0, 0, 0, 0))
fg_glow_draw = ImageDraw.Draw(fg_glow)
fg_glow_draw.ellipse([-50, -50, 450, 550], fill=(0, 229, 255, 45))
fg_glow_draw.ellipse([600, -100, 1100, 600], fill=(168, 85, 247, 50))
fg_glow = fg_glow.filter(ImageFilter.GaussianBlur(55))
fg_canvas = Image.alpha_composite(fg_canvas, fg_glow)

# Place Logo on Left
fg_logo = logo_img.resize((370, 390), Image.Resampling.LANCZOS)
fg_canvas.paste(fg_logo, (50, 55), fg_logo)

# Draw Typography & Messaging
fg_draw = ImageDraw.Draw(fg_canvas)

try:
    title_font = ImageFont.truetype("arial.ttf", 64)
    subtitle_font = ImageFont.truetype("arial.ttf", 34)
    tagline_font = ImageFont.truetype("arial.ttf", 26)
    bullet_font = ImageFont.truetype("arial.ttf", 21)
except:
    title_font = subtitle_font = tagline_font = bullet_font = ImageFont.load_default()

# Title text: NeuroOS
fg_draw.text((440, 60), "NeuroOS", fill=(0, 229, 255, 255), font=title_font)

# Subtitle: The Neurodivergent OS
fg_draw.text((440, 135), "The Neurodivergent OS", fill=(244, 247, 255, 255), font=subtitle_font)

# Tagline: Made by us, for us.
fg_draw.text((440, 185), "Made by us, for us.", fill=(168, 85, 247, 255), font=tagline_font)

# Sub-headline: Reimagining smartphones...
fg_draw.text((440, 225), "Reimagining smartphones for our community.", fill=(203, 213, 225, 255), font=tagline_font)

# Feature Bullets (Clean ASCII bullets, no missing glyph boxes)
bullets = [
    "* SensoryShield -- One-Tap Emergency Sanctuary",
    "* Energy-Aware Planner -- Mental Battery Tracking",
    "* Kids Adventure Mode -- Gamified Routines & Habits",
    "* Tactile Talk Board -- Picture Communication & Phonics"
]

y_pos = 280
for b in bullets:
    fg_draw.text((440, y_pos), b, fill=(203, 213, 225, 255), font=bullet_font)
    y_pos += 34

# Outer Neon Frame
fg_draw.rectangle([2, 2, 1022, 498], outline=(0, 229, 255, 90), width=3)

fg_canvas.save(os.path.join(output_dir, "02_Feature_Graphic_1024x500.png"))
print("Generated: 02_Feature_Graphic_1024x500.png")

# -------------------------------------------------------------
# 3. Formatted Screenshots (1080x1920)
# -------------------------------------------------------------
screenshots = [
    ("refresh_verify.png", "03_Phone_Screenshot_Adult_Mode_1080x1920.png", "EXECUTIVE CLARITY", "One Current Priority & Mental Battery Tracking"),
    ("sc_02_kids.png", "04_Phone_Screenshot_Kids_Mode_1080x1920.png", "KIDS ADVENTURE MODE", "Child-Safe Islands & Tactile Talk Board"),
    ("cyan_dashboard.png", "05_Phone_Screenshot_Sensory_Shield_1080x1920.png", "SENSORY SHIELD", "One-Tap Emergency Sanctuary & Reading Comfort")
]

for src_name, out_name, header, sub in screenshots:
    if os.path.exists(src_name):
        sc_canvas = Image.new("RGBA", (1080, 1920), (5, 8, 17, 255))

        # Background Glow
        sc_glow = Image.new("RGBA", (1080, 1920), (0, 0, 0, 0))
        sc_gdraw = ImageDraw.Draw(sc_glow)
        sc_gdraw.ellipse([100, -100, 980, 700], fill=(0, 229, 255, 35))
        sc_glow = sc_glow.filter(ImageFilter.GaussianBlur(60))
        sc_canvas = Image.alpha_composite(sc_canvas, sc_glow)

        sc_draw = ImageDraw.Draw(sc_canvas)

        try:
            h_font = ImageFont.truetype("arial.ttf", 52)
            sub_font = ImageFont.truetype("arial.ttf", 30)
        except:
            h_font = sub_font = ImageFont.load_default()

        # Draw Header Banner at Top
        sc_draw.text((540, 90), header, fill=(0, 229, 255, 255), font=h_font, anchor="mm")
        sc_draw.text((540, 155), sub, fill=(203, 213, 225, 255), font=sub_font, anchor="mm")

        # Open and place screenshot image inside phone mockup frame
        src_img = Image.open(src_name).convert("RGBA")

        target_w = 880
        target_h = int(src_img.height * (target_w / src_img.width))
        if target_h > 1550:
            target_h = 1550
            target_w = int(src_img.width * (target_h / src_img.height))

        resized_src = src_img.resize((target_w, target_h), Image.Resampling.LANCZOS)

        frame_x = (1080 - target_w) // 2
        frame_y = 240

        sc_canvas.paste(resized_src, (frame_x, frame_y), resized_src)

        # Draw phone bezel around it
        sc_draw.rounded_rectangle([frame_x - 10, frame_y - 10, frame_x + target_w + 10, frame_y + target_h + 10], radius=32, outline=(168, 85, 247, 180), width=5)

        sc_canvas.save(os.path.join(output_dir, out_name))
        print(f"Generated: {out_name}")

print("All Play Store graphics generated successfully!")
