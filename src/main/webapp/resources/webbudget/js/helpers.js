/*
 * Copyright (C) 2016 Arthur Gregorio, AG.Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Fecha dialogs do primefaces
 * 
 * @param {type} widget
 * @returns {undefined}
 */
function closeDialog(widget) {
    PF(widget).hide();
};

/**
 * Invoca o blockUI pelo seu widget var
 * 
 * @param {type} widget
 * @returns {undefined}
 */
function block(widget) {
    PF(widget).block();
};

/**
 * Finaliza o blockUI pelo seu widget var
 * 
 * @param {type} widget
 * @returns {undefined}
 */
function unblock(widget) {
    PF(widget).unblock();
};