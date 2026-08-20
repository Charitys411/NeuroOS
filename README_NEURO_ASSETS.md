# Neuro OS Android assets

All `ic_neuro_*.xml` files are transparent Vector Drawables. Use them with `painterResource(...)` in Compose or as `android:src` in XML layouts. Their paths remain sharp at every density.

`neuro_primary_button.xml` is a state-list drawable for legacy/XML views. In Jetpack Compose, prefer a Material 3 `Button` with `RoundedCornerShape(14.dp)` and theme colors so typography, focus, ripple, disabled state, and accessibility remain native.

`neuro_surface_card.xml` is available for XML layouts. In Compose, prefer `Card(shape = RoundedCornerShape(24.dp))` with the Neuro OS color scheme.

Do not export buttons, cards, switches, search fields, or navigation containers as PNGs. Raster controls blur across densities, cannot adapt to text size, and hide interaction states from accessibility services.
