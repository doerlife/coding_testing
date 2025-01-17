function [isBracketstring] = isBracketstring(string)
%isBracketstring
% a simple test wether the given string contains squared brackets, curly
% brackets or parenthesis
%
% USAGE:
% ======
%
% [boolean] = isBracketString(string)
%
% string: test string
%
% boolean: true if string contains (, ), {, }, [ or ]
%          false otherwise

% Information:
% ============
% Systems Biology Toolbox for MATLAB
% Copyright (C) 2005-2007 Fraunhofer-Chalmers Research Centre
% Author of the toolbox: Henning Schmidt, henning@hschmidt.de
% Programmed by Gunnar Drews, Student at University of
% Rostock Department of Computerscience, gunnar.drews@uni-rostock.de
% 
% This program is free software; you can redistribute it and/or
% modify it under the terms of the GNU General Public License
% as published by the Free Software Foundation; either version 2
% of the License, or (at your option) any later version.
% 
% This program is distributed in the hope that it will be useful,
% but WITHOUT ANY WARRANTY; without even the implied warranty of
% MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
% GNU General Public License for more details. 
% 
% You should have received a copy of the GNU General Public License
% along with this program; if not, write to the Free Software
% Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
% USA.

    if strfind(string, '('),
        isBracketstring = 1;
        return;
    end
    if strfind(string, ')'),
        isBracketstring = 1;
        return;
    end
    if strfind(string, '['),
        isBracketstring = 1;
        return;
    end
    if strfind(string, ']'),
        isBracketstring = 1;
        return;
    end
    if strfind(string, '{'),
        isBracketstring = 1;
        return;
    end
    if strfind(string, '}'),
        isBracketstring = 1;
        return;
    end
    isBracketstring = 0;
return