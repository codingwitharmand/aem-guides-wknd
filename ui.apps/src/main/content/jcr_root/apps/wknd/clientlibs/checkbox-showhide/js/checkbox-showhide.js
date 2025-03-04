/**
 * How to use:
 *
 * 1. Add class to checkbox
 *
 *		Example: granite:class="cq-dialog-checkbox-showhide"
 *
 * 2. Add cq-dialog-checkbox-showhide-target data-attribute to checkbox with the value being the selector to target for toggleing
 *
 *		Example: cq-dialog-checkbox-showhide-target=".togglefield"
 *      Above applies to field that should be shown when checkbox is checked.
 *
 *      Example: cq-dialog-checkbox-showhide-target=".togglefield-hide"
 *      Above applies to field that should be shown when checkbox is unchecked.
 *
 * 3. Add target class to toggleable fields or components
 *
 * 4. Add class cq-dialog-checkbox-manage-required in the required field + required = true in granite:class attribute, but when it's richText add it on wrapperClass attribute
 *
 *	    Example: granite:class="togglefield"
 */
(function (document, $) {
    "use strict";

    // when dialog gets injected
    $(document).on("foundation-contentloaded", function (e) {
        // if there is already an inital value make sure the according target element becomes visible
        checkboxShowHideHandler($(".cq-dialog-checkbox-showhide", e.target));
    });

    $(document).on("change", ".cq-dialog-checkbox-showhide", function (e) {
        checkboxShowHideHandler($(this));
    });

    function checkboxShowHideHandler(el) {
        el.each(function (i, element) {
            if($(element).is("coral-checkbox")) {
                // handle Coral3 base drop-down
                Coral.commons.ready(element, function (component) {
                    showHide(component, element);
                    component.on("change", function () {
                        showHide(component, element);
                    });
                });
            } else {
                // handle Coral2 based drop-down
                let component = $(element).data("checkbox");
                if (component) {
                    showHide(component, element);
                }
            }
        })
    }

    function showHide(component, element) {
        console.log('showing');
        // get the selector to find the target elements. its stored as data-.. attribute
        let target = $(element).data("cqDialogCheckboxShowhideTarget");
        let $target = $(target);
        let targetHide = target+"-hide";
        let $targetHide = $(targetHide);

        if($targetHide){
            $targetHide.removeClass("hide");
            updateRequiredAttributes($targetHide,true);
            if (component.checked) {
                updateRequiredAttributes($targetHide,false);
                $targetHide.addClass("hide");
            }
        }
        if (target) {
            updateRequiredAttributes($target,true);
            $target.addClass("hide");
            if (component.checked) {
                updateRequiredAttributes($target,false);
                $target.removeClass("hide");
            }
        }
    }

    function updateRequiredAttributes($target, hideElement) {
        $target.find('.cq-dialog-checkbox-manage-required, [data-wrapperclass="cq-dialog-checkbox-manage-required"]').each(function(index, element) {
            let $elem = $(element);

            if (hideElement === false) {
                // If the element is visible, ADD attributes
                if ($elem.is("input") || $elem.is("div")) {
                    $elem.attr("aria-required", "true");
                } else if ($elem.is("coral-fileupload")) {
                    $elem.attr("data-cq-fileupload-required", "");
                }
            } else if (hideElement === true) {
                // If the element is hidden, REMOVE attributes
                if ($elem.is("input") || $elem.is("div")) {
                    $elem.removeAttr("aria-required");
                } else if ($elem.is("coral-fileupload")) {
                    $elem.removeAttr("data-cq-fileupload-required");
                }
            }
        });
    }

})(document, Granite.$);


