(function ($) {
    "use strict";

    var tpj = jQuery;
    var revapi24;
    // Preloader 
    jQuery(window).on('load', function () {
        jQuery("#status").fadeOut();
        jQuery("#preloader").delay(350).fadeOut("slow");
    });


    // on ready function
    jQuery(document).ready(function ($) {


        var xv_ww = $(window).width(), xv_slideshow = true;
        $(window).on('resize load', function () {
            xv_ww = $(window).width();
            if (xv_ww <= 767) {
                $('.responsive-menu').removeClass('xv-menuwrapper').addClass('dl-menuwrapper');
                $('.lg-submenu').addClass("dl-submenu");
            } else {
                $('.responsive-menu').removeClass('dl-menuwrapper').addClass('xv-menuwrapper');
                $('.lg-submenu').removeClass("dl-submenu");
            }
        });
        $('#dl-menu').dlmenu({
            animationClasses: {
                classin: 'dl-animate-in-5',
                classout: 'dl-animate-out-5'
            }
        });


        // Menu js for Position fixed
        $(window).scroll(function () {
            var window_top = $(window).scrollTop() + 1;
            if (window_top > 160) {
                $('.prs_navigation_main_wrapper').addClass('menu_fixed animated fadeInDown');
            } else {
                $('.prs_navigation_main_wrapper').removeClass('menu_fixed animated fadeInDown');
            }
        });

        // ===== Scroll to Top ====
        $(window).scroll(function () {
            if ($(this).scrollTop() >= 100) {
                $('#return-to-top').fadeIn(200);
            } else {
                $('#return-to-top').fadeOut(200);
            }
        });
        $('#return-to-top').on('click', function () {
            $('body,html').animate({
                scrollTop: 0
            }, 500);
        });


        /*--------------------------
    nice Select active
    ---------------------------- */
        $('select').niceSelect();


        var tpj = jQuery;
        var revapi41;
        tpj(document).ready(function () {
            if (tpj("#rev_slider_41_1").revolution == undefined) {
            } else {
                revapi41 = tpj("#rev_slider_41_1").show().revolution({
                    sliderType: "carousel",
                    jsFileLocation: "revolution/js/",
                    sliderLayout: "fullwidth",
                    dottedOverlay: "none",
                    delay: 500,
                    navigation: {
                        keyboardNavigation: "off",
                        keyboard_direction: "horizontal",
                        mouseScrollNavigation: "off",
                        mouseScrollReverse: "default",
                        onHoverStop: "off",
                        arrows: {
                            style: "metis",
                            enable: true,
                            hide_onmobile: false,
                            hide_under: 0,
                            hide_onleave: false,
                            tmp: '',
                            left: {
                                h_align: "left",
                                v_align: "center",
                                h_offset: 20,
                                v_offset: 0
                            },
                            right: {
                                h_align: "right",
                                v_align: "center",
                                h_offset: 20,
                                v_offset: 0
                            }
                        }
                        ,
                        bullets: {
                            enable: true,
                            hide_onmobile: false,
                            hide_under: 580,
                            style: "hephaistos",
                            hide_onleave: false,
                            direction: "horizontal",
                            h_align: "center",
                            v_align: "bottom",
                            h_offset: 0,
                            v_offset: 40,
                            space: 10,
                            tmp: ''
                        }
                    },
                    carousel: {
                        maxRotation: 20,
                        vary_rotation: "on",
                        minScale: 0,
                        vary_scale: "off",
                        horizontal_align: "center",
                        vertical_align: "center",
                        fadeout: "on",
                        vary_fade: "on",
                        maxVisibleItems: 3,
                        infinity: "on",
                        space: 0,
                        stretch: "off",
                        showLayersAllTime: "off",
                        easing: "Power3.easeInOut",
                        speed: "800"
                    },
                    visibilityLevels: [1240, 1024, 778, 480],
                    gridwidth: 1200,
                    gridheight: 880,
                    lazyType: "none",
                    shadow: 0,
                    spinner: "off",
                    stopLoop: "off",
                    stopAfterLoops: -1,
                    stopAtSlide: -1,
                    shuffle: "off",
                    autoHeight: "off",
                    disableProgressBar: "on",
                    hideThumbsOnMobile: "off",
                    hideSliderAtLimit: 0,
                    hideCaptionAtLimit: 0,
                    hideAllCaptionAtLilmit: 0,
                    debugMode: false,
                    fallbacks: {
                        simplifyAll: "off",
                        nextSlideOnWindowFocus: "off",
                        disableFocusListener: false,
                    }
                });
            }
        });	/*ready*/


        // Featured Products Js
        $('.prs_upcom_slider_slides_wrapper .owl-carousel').owlCarousel({
            loop: true,
            margin: 0,
            nav: true,
            autoplay: false,
            smartSpeed: 1200,
            navText: ["<i class='flaticon-left-arrow'></i>", "<i class='flaticon-right-arrow'></i>"],
            dots: false,
            autoplayHoverPause: true,
            responsive: {
                0: {
                    items: 1
                },
                600: {
                    items: 1
                },
                1000: {
                    items: 1
                }
            }
        });


        $(document).ready(function () {
            $('.prs_feature_slider_wrapper .owl-carousel').owlCarousel({
                loop: true,
                margin: 10,
                autoplay: true,
                responsiveClass: true,
                smartSpeed: 1200,
                navText: ['<i class="flaticon-play-button"></i>', '<i class="flaticon-play-button"></i>'],
                responsive: {
                    0: {
                        items: 1,
                        nav: true
                    },
                    500: {
                        items: 1,
                        nav: true
                    },
                    700: {
                        items: 2,
                        nav: true
                    },
                    1000: {
                        items: 3,
                        nav: true,
                        loop: true,
                        margin: 20
                    }
                }
            })
        })


        $(document).ready(function () {
            $('.prs_es_speak_slider_wrapper .owl-carousel').owlCarousel({
                loop: true,
                margin: 10,
                autoplay: true,
                responsiveClass: true,
                smartSpeed: 1200,
                navText: ['<i class="flaticon-play-button"></i>', '<i class="flaticon-play-button"></i>'],
                responsive: {
                    0: {
                        items: 1,
                        nav: true
                    },
                    500: {
                        items: 2,
                        nav: true
                    },
                    700: {
                        items: 3,
                        nav: true
                    },
                    1000: {
                        items: 4,
                        nav: true,
                        loop: true,
                        margin: 20
                    }
                }
            })
        })

        $(document).ready(function () {
            $('.prs_vp_center_slider .owl-carousel').owlCarousel({
                loop: true,
                margin: 10,
                autoplay: true,
                responsiveClass: true,
                smartSpeed: 1200,
                navText: ['<i class="flaticon-play-button"></i>', '<i class="flaticon-play-button"></i>'],
                responsive: {
                    0: {
                        items: 1,
                        nav: true
                    },
                    500: {
                        items: 1,
                        nav: true
                    },
                    700: {
                        items: 1,
                        nav: true
                    },
                    1000: {
                        items: 1,
                        nav: true,
                        loop: true,
                        margin: 20
                    }
                }
            })
        })


        $(document).ready(function () {
            $('.prs_ms_trailer_slider_left_wrapper .owl-carousel').owlCarousel({
                loop: true,
                margin: 10,
                autoplay: true,
                responsiveClass: true,
                smartSpeed: 1200,
                navText: ['<i class="flaticon-play-button"></i>', '<i class="flaticon-play-button"></i>'],
                responsive: {
                    0: {
                        items: 1,
                        nav: true
                    },
                    500: {
                        items: 1,
                        nav: true
                    },
                    700: {
                        items: 1,
                        nav: true
                    },
                    1000: {
                        items: 1,
                        nav: true,
                        loop: true,
                        margin: 20
                    }
                }
            })
        })


        $(document).ready(function () {
            $('.prs_ms_scene_slider_wrapper .owl-carousel').owlCarousel({
                loop: true,
                margin: 10,
                autoplay: true,
                responsiveClass: true,
                smartSpeed: 1200,
                navText: ['<i class="flaticon-play-button"></i>', '<i class="flaticon-play-button"></i>'],
                responsive: {
                    0: {
                        items: 1,
                        nav: true
                    },
                    500: {
                        items: 2,
                        nav: true
                    },
                    700: {
                        items: 3,
                        nav: true
                    },
                    1000: {
                        items: 3,
                        nav: true,
                        loop: true,
                        margin: 20
                    }
                }
            })
        })

        $(document).ready(function () {
            $('.prs_pn_slider_wraper .owl-carousel').owlCarousel({
                loop: true,
                margin: 10,
                autoplay: true,
                responsiveClass: true,
                smartSpeed: 1200,
                navText: ['<i class="flaticon-play-button"></i>', '<i class="flaticon-play-button"></i>'],
                responsive: {
                    0: {
                        items: 1,
                        nav: true
                    },
                    500: {
                        items: 3,
                        nav: true
                    },
                    700: {
                        items: 4,
                        nav: true
                    },
                    1000: {
                        items: 6,
                        nav: true,
                        loop: true,
                        margin: 20
                    }
                }
            })
        })


        $(document).ready(function () {
            $('.prs_about_pre_right_slider_sec_wrapper .owl-carousel').owlCarousel({
                loop: true,
                margin: 10,
                autoplay: true,
                responsiveClass: true,
                smartSpeed: 1200,
                navText: ['<i class="flaticon-play-button"></i>', '<i class="flaticon-play-button"></i>'],
                responsive: {
                    0: {
                        items: 1,
                        nav: true
                    },
                    500: {
                        items: 1,
                        nav: true
                    },
                    700: {
                        items: 1,
                        nav: true
                    },
                    1000: {
                        items: 1,
                        nav: true,
                        loop: true,
                        margin: 20
                    }
                }
            })
        })


        $(document).ready(function () {
            $('.prs_mc_slider_wrapper .owl-carousel').owlCarousel({
                loop: true,
                margin: 10,
                autoplay: true,
                responsiveClass: true,
                smartSpeed: 1200,
                navText: ['<i class="flaticon-play-button"></i>', '<i class="flaticon-play-button"></i>'],
                responsive: {
                    0: {
                        items: 1,
                        nav: true
                    },
                    500: {
                        items: 1,
                        nav: true
                    },
                    700: {
                        items: 1,
                        nav: true
                    },
                    1000: {
                        items: 1,
                        nav: true,
                        loop: true,
                        margin: 20
                    }
                }
            })
        })

        $(document).ready(function () {
            $('.prs_navi_slider_wraper .owl-carousel').owlCarousel({
                loop: true,
                margin: 10,
                autoplay: true,
                responsiveClass: true,
                smartSpeed: 1200,
                navText: ['<i class="flaticon-play-button"></i>', '<i class="flaticon-play-button"></i>'],
                responsive: {
                    0: {
                        items: 1
                    },
                    600: {
                        items: 1
                    },
                    1000: {
                        items: 1
                    }
                }
            })
        })


        $(document).ready(function () {
            $('.prs_ms_rm_slider_wrapper .owl-carousel').owlCarousel({
                loop: true,
                margin: 10,
                autoplay: true,
                responsiveClass: true,
                smartSpeed: 1200,
                navText: ['<i class="flaticon-play-button"></i>', '<i class="flaticon-play-button"></i>'],
                responsive: {
                    0: {
                        items: 1
                    },
                    600: {
                        items: 2
                    },
                    1000: {
                        items: 4
                    }
                }
            })
        })


        $(document).ready(function () {
            $('.hs_blog_box1_img_wrapper .owl-carousel').owlCarousel({
                loop: true,
                margin: 10,
                autoplay: true,
                responsiveClass: true,
                smartSpeed: 1200,
                navText: ['<i class="flaticon-play-button"></i>', '<i class="flaticon-play-button"></i>'],
                responsive: {
                    0: {
                        items: 1,
                        nav: true
                    },
                    600: {
                        items: 1,
                        nav: true
                    },
                    1000: {
                        items: 1,
                        nav: true,
                        loop: true,
                        margin: 20
                    }
                }
            })
        })

        $(document).ready(function () {
            $('.st_md_summ_client_slider .owl-carousel').owlCarousel({
                loop: true,
                margin: 10,
                autoplay: false,
                responsiveClass: true,
                smartSpeed: 1200,
                navText: ['<i class="flaticon-left-arrow" aria-hidden="true"></i>', '<i class="flaticon-right-arrow" aria-hidden="true"></i>'],
                responsive: {
                    0: {
                        items: 2,
                        nav: true
                    },
                    600: {
                        items: 3,
                        nav: true
                    },
                    1000: {
                        items: 4,
                        nav: true,
                        loop: true,
                        margin: 20
                    }
                }
            })
        })


        $(".album-slider").bxSlider({
            minSlides: 1,
            maxSlides: 10,
            slideWidth: 257,
            slideMargin: 17,
            ticker: true,
            tickerHover: true,
            speed: 20000,
            useCSS: false,
            infiniteLoop: false

        });

        $(".movie-cast-slider").bxSlider({
            minSlides: 1,
            maxSlides: 10,
            slideWidth: 257,
            slideMargin: 17,
            ticker: true,
            tickerHover: true,
            speed: 20000,
            useCSS: false,
            infiniteLoop: false

        });


        $(".prs_vp_left_slider").bxSlider({
            minSlides: 1,
            autoDirection: 'next',
            mode: 'vertical',
            maxSlides: 10,
            slideWidth: 257,
            slideMargin: 17,
            ticker: true,
            tickerHover: true,
            speed: 15000,
            useCSS: false,
            infiniteLoop: false

        });

        $(".prs_vp_right_slider").bxSlider({
            minSlides: 1,
            mode: 'vertical',
            autoDirection: 'prev',
            maxSlides: 10,
            slideWidth: 257,
            slideMargin: 17,
            ticker: true,
            tickerHover: true,
            speed: 15000,
            useCSS: false,
            infiniteLoop: false

        });

        /* 09. VENOBOX JS */
        $('.venobox').venobox({
            numeratio: true,
            titleattr: 'data-title',
            titlePosition: 'top',
            spinner: 'wandering-cubes',
            spinColor: '#007bff'
        });


        // Magnific popup-video

        $('.test-popup-link').magnificPopup({
            type: 'iframe',
            iframe: {
                markup: '<div class="mfp-iframe-scaler">' +
                    '<div class="mfp-close"></div>' +
                    '<iframe class="mfp-iframe" frameborder="0" allowfullscreen></iframe>' +
                    '<div class="mfp-title">Some caption</div>' +
                    '</div>',
                patterns: {
                    youtube: {
                        index: 'youtube.com/',
                        id: 'v=',
                        src: 'https://www.youtube.com/embed/%id%?autoplay=1'
                    }
                }
            }
            // other options
        });


        $('.second-nav-toggler').on('click', function (e) {
            e.preventDefault();
            var mask = '<div class="mask-overlay">';

            $('body').toggleClass('active');
            $(mask).hide().appendTo('body').fadeIn('fast');
            $('.mask-overlay, .manu-close').on('click', function () {
                $('body').removeClass('active');
                $('.mask-overlay').remove();
            });
        });


        //show hide login form js
        $('#search_button').on("click", function (e) {
            $('#search_open').slideToggle();
            e.stopPropagation();
        });

        $(document).on("click", function (e) {
            if (!(e.target.closest('#search_open'))) {
                $("#search_open").slideUp();
            }
        });

        //-------------------------------------------------------
        // counter-section
        //-------------------------------------------------------
        $('.counter-section').on('inview', function (event, visible, visiblePartX, visiblePartY) {
            if (visible) {
                $(this).find('.timer').each(function () {
                    var $this = $(this);
                    $({Counter: 0}).animate({Counter: $this.text()}, {
                        duration: 2000,
                        easing: 'swing',
                        step: function () {
                            $this.text(Math.ceil(this.Counter));
                        }
                    });
                });
                $(this).off('inview');
            }
        });


        // -------------------------------------------------------------
// Shuffle
// -------------------------------------------------------------

        $(window).load(function () {
            /** this is come when complete page is fully loaded, including all frames, objects and images **/

            if ($('#gridWrapper').length > 0) {

                /* initialize shuffle plugin */
                var $grid = $('#gridWrapper');

                $grid.shuffle({
                    itemSelector: '.portfolio-wrapper' // the selector for the items in the grid
                });

                /* reshuffle when user clicks a filter item */
                $('#filter a').on('click', function (e) {
                    e.preventDefault();

                    // set active class
                    $('#filter a').removeClass('active');
                    $(this).addClass('active');

                    // get group name from clicked item
                    var groupName = $(this).attr('data-group');

                    // reshuffle grid
                    $grid.shuffle('shuffle', groupName);
                });
            }
        });


        /*********color change script start*******/
        $('.colorchange').on('click', function () {

            var color_name = $(this).attr('id');
            var new_style = 'css/color/' + color_name + '.css';
            $('#theme-color').attr('href', new_style);


        });
        //rotate setting gear
        $(function () {

            var $rota = $('#style-switcher .bottom a.settings img'),
                degree = 0,
                timer;

            function rotate() {
                $rota.css({transform: 'rotate(' + degree + 'deg)'});
                // timeout increase degrees:
                timer = setTimeout(function () {
                    ++degree;
                    rotate(); // loop it
                }, 0);
            }

            rotate();    // run it!

        });


        $("#style-switcher .bottom a.settings").on('click', function (e) {
            e.preventDefault();
            var div = $("#style-switcher");
            if (div.css("left") === "-161px") {
                $("#style-switcher").animate({
                    left: "0px"
                });
            } else {
                $("#style-switcher").animate({
                    left: "-161px"
                });
            }
        });


        /******color change script end******/


    });

})(jQuery);