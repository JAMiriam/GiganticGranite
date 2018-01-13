<?php
    require_once 'info_logic.php';
?>

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
                            <a class="nav-link" href="index.php">Sing in</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="register.php">Sign up</a>
                        </li>
                        <?php
                    } ?>
                </ul>
            </div>
        </nav>
        
        <script>
            <?php echo $variables; ?>
        </script>
        
        <!-- TODO -->
        <!-- modal for suggestions -->
        <div id="myModal" class="modal fade" role="dialog">
            <div class="modal-dialog">
            
            <!-- Modal content-->
            <div class="modal-content">
              <div class="modal-header">
                <h4 class="modal-title">Who is it?</h4>
              </div>
              <div class="modal-body text-center">
                  <input id="actorNameInput" type="text" placeholder="Actor name" class="form-control">
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-default" data-dismiss="modal" onclick="sendSuggestion()">Submit</button>
              </div>
            </div>
          
            </div>
        </div>
        
        <div id="confirmModal" class="modal fade" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-body">
                        <p class="text-center">Thank You for your suggestion. We'll add this actor as soon as we've got enough pictures.</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- fill main -->
        <main>
            <div class="container text-center jumbotron">
                <img src="<?php echo $src ?>" class="big-image" onclick="imgClick(event)">
            </div>
            <div class="jumbotron">
                <div class="container" id="ajax-container">

                </div>
            </div>
        </main>

        <!-- JQuery, Popper.js, Bootstrap JS scripts -->
        <script src="https://code.jquery.com/jquery-3.2.1.min.js" integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
        <script src="scripts/ajax.js"></script>
        <script src="scripts/info.js"></script>
    </body>
</html>