<?php require_once 'utils.php' ?>

<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Gigantic Granite</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
        <link href="css/style.css" rel="stylesheet">
    </head>
    <body>
        <!-- navbar -->
        <nav class="navbar navbar-expand fixed-top bg-light">
            <div class="container">
                <a class="navbar-brand" href="/">Gigantic Granite</a>
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link" href="upload.php">Upload</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="history.php">History</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="about.php">About</a>
                    </li>
                </ul>
                <ul class="navbar-nav ml-auto">
                    <?php
                    if (signed_in()) { ?>
                        <li class="nav-item">
                            <a class="nav-link disabled"><?php echo $_SESSION['username']; ?></a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="logout.php">Log out</a>
                        </li>
                        <?php
                    } else { ?>
                        <li class="nav-item">
                            <a class="nav-link" href="#top">Sing in</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="register.php">Sign up</a>
                        </li>
                        <?php
                    } ?>
                </ul>
            </div>
        </nav>

        <main>
            <?php
            if (signed_in()) { ?>
                <div class="container text-center" id="top">
                    <h2>Welcome back!</h2>
                    <a href="upload.php">Upload image</a>
                </div>
                <?php
            } elseif (isset($_POST['username']) && isset($_POST['password'])) {
                sign_in($_POST['username'], $_POST['password']);
            } else { ?>

                <!-- sign in -->            
                <div class="container" id="top">
                    <form class="form-signin" action="/" method="post">
                        <h2 class="form-signin-heading">Sign in</h2>
                        <div class="form-group">
                            <label for="inputUsername" class="sr-only">Username</label>
                            <input type="text" id="inputUsername" name="username" class="form-control" placeholder="Username" required autofocus>
                            <?php
                            if (isset($_GET['error']) && $_GET['error'] == 1) { ?>
                                <span class="help-block">Wrong username</span>
                                <?php
                            } ?>
                        </div>
                        <div class="form-group">
                            <label for="inputPassword" class="sr-only">Password</label>
                            <input type="password" id="inputPassword" name="password" class="form-control" placeholder="Password" required>
                            <?php
                            if (isset($_GET['error']) && $_GET['error'] == 2) { ?>
                                <span class="help-block">Wrong password</span>
                                <?php
                            } ?>
                        </div>
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" value="remember-me"> Remember me
                            </label>
                        </div>
                        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
                    </form>
                </div>
                
                <div class="container text-center">
                    Don't have an account? <a href='register.php'>Create</a> it or start <a href="upload.php">uploading</a> your screenshots without it.
                </div>
                
                <?php
            } ?>
        </main>
        <!-- JQuery, Popper.js, Bootstrap JS scripts -->
        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
    </body>
</html>