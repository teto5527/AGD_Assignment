package com.mygdx.game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class LayerRenderer extends OrthogonalTiledMapRenderer {
    public LayerRenderer(TiledMap map) {
        super(map);
    }

    public void renderTileLayer(TiledMapTileLayer layer) {
        beginRender();
        super.renderTileLayer(layer);
        endRender();
    }
}
