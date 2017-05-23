from flask import Flask, render_template, request
from feedline import feedline

app = Flask(__name__)

@app.route("/")
def initiate():
	return render_template("mypython.html")

history = []

@app.route('/handle_feedline', methods=['POST'])
def handle_feedline():
    assert request.method == 'POST'

    string = request.form["string"]

    history.append(string+": "+feedline(string))
    return render_template('mypython.html', feedback=history)


if __name__ == "__main__":
    app.run()
