$(document).ready(function(){
    $('.entitiesList #action_button').on('click', function(event){
        event.preventDefault();
        var deleteLink = $(this).attr('href');

        if (deletePermission == "true") {
            $('#modal_deletion_confirmation #button_submit_modal_window_deletion_confirmation').attr('href', deleteLink);
            $("#modal_deletion_confirmation").fadeIn("slow");
            $("#button_submit_modal_window_deletion_confirmation").click(function () {
                $(".overlay_deletion_confirmation").fadeOut("slow");
            });
            $(".modal_window_deletion_confirmation__close").click(function () {
                $(".overlay_deletion_confirmation").fadeOut("slow");
            });
            $(".cancelConfirmDelete").click(function () {
                $(".overlay_deletion_confirmation").fadeOut("slow");
            });
        } else {
            $("#modal_deletion_prohibited").fadeIn("slow");
            $(".cancelButton").click(function () {
                $(".overlay_deletion_prohibited").fadeOut("slow");
            });
            $(".modal_window_deletion_update_prohibited__close").click(function () {
                $(".overlay_deletion_prohibited").fadeOut("slow");
            });
        };

        if(updatePermission == "false") {
            $("#modal_update_prohibited").fadeIn("slow");
            $(".cancelButton").click(function () {
                $(".overlay_update_prohibited").fadeOut("slow");
            });
            $(".modal_window_deletion_update_prohibited__close").click(function () {
                $(".overlay_update_prohibited").fadeOut("slow");
            });
        } else {
            $(".entitiesList").submit();
        };

    });
});