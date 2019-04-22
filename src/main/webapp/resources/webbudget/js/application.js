$(function () {
    'use strict';

    /**
     * Function to setup all the configuration of the template
     */
    function setup() {
        // use this to instantly change the sidebar color
        $('[data-sidebarskin="toggle"]').on('click', function () {
            toggleSidebarColor()
        })
    }

    setup();
});

/**
 * Function to toggle the configuration sidebar color
 */
function toggleSidebarColor() {
    const $sidebar = $('.control-sidebar')

    if ($sidebar.hasClass('control-sidebar-dark')) {
        $sidebar.removeClass('control-sidebar-dark')
        $sidebar.addClass('control-sidebar-light')
    } else {
        $sidebar.removeClass('control-sidebar-light')
        $sidebar.addClass('control-sidebar-dark')
    }
}

/**
 * Function to help the process of changing the application theme
 *
 * @param cls the class of the new skin to set
 */
function changeSkin(cls) {

    const skins = [
        'skin-blue',
        'skin-black',
        'skin-red',
        'skin-yellow',
        'skin-purple',
        'skin-green'
    ];

    $.each(skins, function (i) {
        $('body').removeClass(skins[i])
    });

    $('body').addClass(cls);
}