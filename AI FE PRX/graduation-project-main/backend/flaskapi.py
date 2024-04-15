import os
import numpy as np
import io
import base64
from dash import dcc, html, Input, Output, no_update
import plotly.graph_objects as go
import dash
from flask import Flask, request
from flask_cors import CORS
from PIL import Image
from copy import deepcopy
from sklearn.manifold import TSNE

# import sys
# print(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))
# sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))
import Recommander
from models import Encoders
from utils import pickleIO, params
from utils.flask_helper.vis_plotly import vis_hover_img_test

server = Flask(__name__)
CORS(server)
app = dash.Dash(__name__, server=server)


@server.route('/meme', methods=['GET'])
def getMeme():
    print(f'[Flask]: getMeme Starts {request.get_json()}')
    msg = request.get_json()['message']
    return dict(result = Recommander.recommand(msg))


# Helper functions
def np_image_to_base64(im_matrix):
    im = Image.fromarray(im_matrix)
    buffer = io.BytesIO()
    im.save(buffer, format="jpeg")
    encoded_image = base64.b64encode(buffer.getvalue()).decode()
    im_url = "data:image/jpeg;base64, " + encoded_image
    return im_url

galaxy = vis_hover_img_test(cluster_fname=params.cluster_fname, vis_ratio=0.1)
print('Clustering: start')
# tsne = TSNE(n_components=3, random_state=3, n_iter=2500, learning_rate='auto', n_jobs=4).fit_transform(galaxy['feature'])
tsne = TSNE(n_components=3, random_state=3, n_iter=4000, learning_rate='auto', n_jobs=4).fit_transform(galaxy['feature'])
print('Clustering: complete')
del galaxy['feature']

home = deepcopy(galaxy['color'][:])
fig = go.Figure(data=[
    go.Scatter3d(
        x=tsne[:, 0],
        y=tsne[:, 1],
        z=tsne[:, 2],
        mode='markers',
        marker=dict(
            size=10,
            color=galaxy['color'],
        ),
    )
])
# del tsne
print('Rendering: complete')

fig.update_traces(
    hoverinfo="none",
    hovertemplate=None,
)

app.layout = html.Div(
    className="container",
    children=[
        dcc.Graph(id="graph-5", figure=fig, clear_on_unhover=True, style={'height': '100vh'}),
        dcc.Tooltip(id="graph-tooltip-5", direction='bottom', style={'margin': '30px'}),
        html.Div(id='hidden-div', style={'display': 'none'})
    ],
)

@app.callback(
    Output("graph-5", "figure"),
    Input("graph-5", "clickData"),
)
def update_chart(clickData):
    # print(clickData)
    print('clicked!')
    if clickData is None:
        return no_update
    if clickData["points"][0]['marker.color']=='rgba(255,255,255,0.3)':
        fig.update_traces(
            dict(marker=dict(
                color=home
            ))
        )
        return fig
    if clickData is not None:
        c = list(fig.data[0].marker.color)
        clan = clickData["points"][0]['marker.color']
        other = 'rgba(255,255,255,0.3)'
        new_c = [color if color==clan else other for color in c]
        fig.update_traces(
            dict(marker=dict(
                color=new_c,
            ))
        )
        return fig


@app.callback(
    Output("graph-tooltip-5", "show"),
    Output("graph-tooltip-5", "bbox"),
    Output("graph-tooltip-5", "children"),
    Input("graph-5", "hoverData"),
)
def display_hover(hoverData):
    if hoverData is None:
        return False, no_update, no_update

    hover_data = hoverData["points"][0]
    bbox = hover_data["bbox"]
    num = hover_data["pointNumber"]

    im_matrix = np.array( Image.open(os.path.join(params.data_root, 'images', galaxy['fname'][num])) )
    im_url = np_image_to_base64(im_matrix)
    children = [
        html.Div([
            html.Img(
                src=im_url,
                style={"height": "256px", 'display': 'block', 'margin': '0 auto'},
            ),
            html.P("Cluster " + str(galaxy['cluster_id'][num]), style={'fontWeight': 'bold'})
        ]),
    ]

    return True, bbox, children

if __name__ == "__main__":
    app.run_server(debug=True, host='0.0.0.0', port=6006)
