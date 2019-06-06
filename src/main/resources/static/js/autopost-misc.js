jQuery(document).ready(function ($) {

    //Makes table row clickable
    $(".clickable-row").click(function () {
        window.location = $(this).data("href");
    });


    //Makes table row clickable
    $(".non-clickable").click(function () {
        window.location = $(this).data("href");
    });


    $(document).ready(function(){
        $('[data-toggle="tooltip"]').tooltip();
    });


    //stops bubbeling of clickable row in checkbox
    $(".checkbox").click(function (event) {
        event.stopImmediatePropagation();
    });


    //stops bubbeling of clickable row in checkbox
    $(".btn-delete-account").click(function (event) {
        let answer = confirm("Do you realy want to delete your account? This will remove all of your autoPost-data irreversible ")
        if(answer === true){
            window.location.href="/deleteaccount";
        }
    });


    $('#select-all').on('click',function(){
        if(this.checked){
            $('.checkbox').each(function(){
                this.checked = true;
            });
        }else{
            $('.checkbox').each(function(){
                this.checked = false;
            });
        }
    });

    //Lightbox preview for post images
    $(document).on('click', '[data-toggle="lightbox"]', function (event) {
        event.preventDefault();
        $(this).ekkoLightbox();
    });


    //Emoji selector
    $(function () {
        window.emojiPicker = new EmojiPicker({
            emojiable_selector: '[data-emojiable=true]',
            assetsPath: '/emoji-picker/img/',
            popupButtonClasses: 'fa fa-smile-o'
        });
        window.emojiPicker.discover();
        //set preserve formatting on wysiwyg editor
        $(".emoji-wysiwyg-editor").addClass("text-input");
    });


    //activate tab toggle in post editor
    $('#imagetype a').on('click', function (e) {
        e.preventDefault()
        $(this).tab('show')
    });






});