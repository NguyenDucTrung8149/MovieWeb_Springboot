var player = videojs('my-video', {
    plugins: {
        httpSourceSelector: {
            default: 'auto' // Tự động chọn chất lượng
        }
    }
});

var movieId = player.el().getAttribute('data-movie-id'); // Lấy movie ID từ thuộc tính data-movie-id

player.ready(function () {
    console.log('Video.js player is ready');

    // -------------------- Nút tua lùi --------------------
    var Button = videojs.getComponent('Button');
    var rewindButton = videojs.extend(Button, {
        constructor: function () {
            Button.apply(this, arguments);
            this.addClass('vjs-rewind-button');
            this.controlText('Rewind 10 seconds');
        },
        handleClick: function () {
            var currentTime = player.currentTime();
            player.currentTime(Math.max(0, currentTime - 10)); // Tua lùi 10 giây
        }
    });
    videojs.registerComponent('RewindButton', rewindButton);
    player.getChild('controlBar').addChild('RewindButton', {}, 1);

    // -------------------- Nút tua tới --------------------
    var forwardButton = videojs.extend(Button, {
        constructor: function () {
            Button.apply(this, arguments);
            this.addClass('vjs-forward-button');
            this.controlText('Forward 10 seconds');
        },
        handleClick: function () {
            var currentTime = player.currentTime();
            player.currentTime(Math.min(player.duration(), currentTime + 10)); // Tua tới 10 giây
        }
    });
    videojs.registerComponent('ForwardButton', forwardButton);
    player.getChild('controlBar').addChild('ForwardButton', {}, 2);

    // -------------------- Nút tăng tốc phát --------------------
    var SpeedButton = videojs.extend(Button, {
        constructor: function () {
            Button.apply(this, arguments);
            this.addClass('vjs-speed-button');
            this.controlText('Speed');
            this.el().innerText = 'Speed: 1x';
        },
        handleClick: function () {
            var speedOptions = [0.5, 1, 1.25, 1.5, 2];
            var currentSpeed = player.playbackRate();
            var nextSpeed = speedOptions[(speedOptions.indexOf(currentSpeed) + 1) % speedOptions.length];
            player.playbackRate(nextSpeed);
            this.el().innerText = `Speed: ${nextSpeed}x`;
        }
    });
    videojs.registerComponent('SpeedButton', SpeedButton);
    player.getChild('controlBar').addChild('SpeedButton', {}, 3);

    // -------------------- Menu chọn chất lượng --------------------
    player.httpSourceSelector(); // Kích hoạt plugin httpSourceSelector

    // -------------------- Sự kiện video --------------------
    player.on('play', function () { console.log('Video is playing'); });
    player.on('pause', function () { console.log('Video is paused'); });

    player.on('timeupdate', function () {
        console.log('Current Time:', player.currentTime());
        if (player.currentTime() >= 10) {
            console.log('Time reached 10 seconds');
            incrementView(movieId);
            player.off('timeupdate');
        }
    });
});

// -------------------- Gửi request tăng lượt xem --------------------
function incrementView(movieId) {
    fetch(`/api/incrementView/${movieId}`, { method: 'PUT' })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            console.log('Response status:', response.status);
            return response.text();
        })
        .then(data => console.log('Response data:', data))
        .catch(error => console.error('Error:', error));
}
