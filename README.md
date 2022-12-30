# TODO
render one sprite

render multiple sprites

make mirror walls

find a better way to stop the player from getting too close to walls

make Toolkit.getDefaultToolkit().sync(); only execute when the os is linux/not windows

add mouse look


first walls were rendered with rectangles

then sliced up bufferedimages

to render one sprite put wall slices or whatever objects are being used in an arraylist and render then in order of decending distance to the player (either seperate bufferedimage arraylist or include corresponding bufferedimage in each ray object, used to use Double[] arraylist)

to render multiple sprites put sprites in an arraylist and render both arraylists in decending distance to the player

to make directional sprites, probably compare the angle the sprite is facing and the angle of the player

idk how to do cellings, floors, or that distorted vertical look
