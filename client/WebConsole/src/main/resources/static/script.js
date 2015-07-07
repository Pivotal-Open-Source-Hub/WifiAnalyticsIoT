var beacons = [
    {label: "b1", x: 200, y: 200, color: "#2069ac", active: true},
    {label: "b2", x: 320, y: 240, color: "#358913", active: true},
    {label: "b3", x: 140, y: 300, color: "#b10292", active: true},
    {label: "b4", x: 240, y: 100, color: "#b1a602", active: false},
    {label: "b5", x: 900, y: 300, color: "#b15402", active: false}
]


function drawCircle(ctx, x, y, radius, color) {
    var maXRadius = 2000; //browser may throw errors for big radius
    if (radius > maXRadius) {
        return;
    }
    ctx.lineWidth = 1;
    ctx.strokeStyle = color;
    ctx.setLineDash([2, 3]);
    ctx.beginPath();
    ctx.arc(x, y, radius, 0, 2 * Math.PI, false);
    ctx.closePath();
    ctx.stroke();
}

function canvas() {
    return document.getElementById('canvas');
}

function activeBeacons() {
    return beacons.filter(function (beacon) {
        return beacon.active;
    });
}

function main() {
    draw();
}

function draw() {
    var ctx = canvas().getContext('2d');
    ctx.canvas.width = window.innerWidth;
    ctx.canvas.height = window.innerHeight;
    ctx.clearRect(0, 0, canvas().width, canvas().height)
    activeBeacons().forEach(function (beacon) {
        drawPoint(ctx, beacon.x, beacon.y, beacon.label, beacon.color);
        var radius = beacon.measuredDistance;
        drawCircle(ctx, beacon.x, beacon.y, radius, beacon.color);
    });

    //drawPoint(ctx, position.x, position.y, "X", position.color);
    //drawPoint(ctx, esitmatedPosition.x, esitmatedPosition.y, "E", esitmatedPosition.color);
}

function drawPoint(ctx, x, y, label, color) {
    ctx.beginPath();
    var radius = 11;
    ctx.arc(x, y, radius, 0, 2 * Math.PI, false);
    ctx.closePath();
    ctx.fillStyle = color;
    ctx.fill();
    ctx.fillStyle = "#ffffff";
    ctx.font = "12px Arial";
    var metrics = ctx.measureText(label);
    ctx.fillText(label, x - metrics.width / 2, y + 12 / 1.25 / 2);
}

function drawCircle(ctx, x, y, radius, color) {
    var maXRadius = 2000; //browser may throw errors for big radius
    if (radius > maXRadius) {
        return;
    }
    ctx.lineWidth = 1;
    ctx.strokeStyle = color;
    ctx.setLineDash([2, 3]);
    ctx.beginPath();
    ctx.arc(x, y, radius, 0, 2 * Math.PI, false);
    ctx.closePath();
    ctx.stroke();
}
