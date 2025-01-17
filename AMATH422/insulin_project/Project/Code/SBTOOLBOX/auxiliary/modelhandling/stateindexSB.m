function [output] = stateindexSB(model,statename)
% stateindexSB: returns the number of the state 'statename' in model
% 'model'. If the state does not exist then [] is returned.
%
% Output Arguments:
% =================
% output = index of the state 'statename' in the model.
%          If 'statename' is not a state in the model, [] is returned.

% Information:
% ============
% Systems Biology Toolbox for MATLAB
% Copyright (C) 2005-2007 Fraunhofer-Chalmers Research Centre
% Author of the toolbox: Henning Schmidt, henning@hschmidt.de
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
% Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

if strcmp(class(statename),'char'),
    statename = {statename};
end

allstates = SBstates(model);

if length(statename) == 1,
    output = strmatch(statename,allstates,'exact');
    if isempty(output),
        output = [];
    end
else    
    output = [];
    for k = 1:length(statename),
        index = strmatch(statename{k},allstates,'exact');
        if isempty(index),
            output(k) = -1;
        else
            output(k) = index;
        end
    end
end
return