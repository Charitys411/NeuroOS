# NeuroOS mockup asset map

The seven supplied PNGs are visual references, not runtime screens. Their content is separated by implementation type so dynamic data and accessibility remain native.

## Native text

All reusable labels and messages are in `res/values/neuro_mockup_strings.xml`. Percentages, dates, device counts, temperatures, network rates, timestamps, and scores are runtime data and must not be baked into images.

## Raster artwork

- `drawable-nodpi/neuro_logo_title_n.png` — standalone Neuro N mark.
- `drawable-nodpi/neuro_feature_graphic.png` — decorative neural/circuit backdrop.
- `drawable-nodpi/neuro_icon_shield.png` — high-detail security hero illustration.
- `drawable-nodpi/neuro_icon_activity.png` — high-detail performance/activity hero illustration.
- `drawable-nodpi/neuro_icon_network.png` — high-detail network hero illustration.
- `drawable-nodpi/neuro_ui_icon_atlas.png` — transparent production reference atlas containing isolated symbols only. It is an authoring source; do not display the whole atlas in the app.

## Native/scalable UI

Rebuild these with Compose or XML rather than raster screenshots:

- Cyan-to-violet outer screen frame.
- Dark navy cards and metric rows.
- Status pills and glowing dots.
- Progress bars, circular gauges, charts, dividers, and bottom navigation.
- Buttons, ellipsis menus, labels, values, and accessibility descriptions.

Use the existing `neuro_surface_card.xml` and `neuro_primary_button.xml` as the baseline for reusable surfaces.

## Icon atlas order

Left-to-right, top-to-bottom:

1. Neuro N
2. Brain
3. Shield lock
4. Battery
5. Clock
6. Thermometer
7. Performance bars
8. Wi-Fi
9. Globe
10. Network topology
11. CPU
12. Memory
13. Storage
14. Activity
15. Calendar
16. Flame
17. Focus target
18. Check shield
19. Bluetooth
20. Link
21. Lightning
22. Connected globe
23. Diagnostics
24. Insight orb
25. Home
26. Modules
27. Insights chart
28. Settings
29. Tools
30. Secure vault

Each final Android drawable must contain one symbol only. Never reference the complete atlas or a flattened screen as a functional icon.

