/*
    Drizzle - A general Myst tool.
    Copyright (C) 2008  Dustin Bernard.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/ 

package prpobjects;

import shared.Flt;
import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.e;
import shared.m;
import shared.b;
//import java.util.Vector;

/**
 *
 * @author user
 */
public class x0006Layer extends uruobj
{
    //Objheader xheader;
    public x0041LayerInterface parent;
    public int flags1;
    public int flags2;
    public int flags3;
    public int flags4;
    public int flags5;
    public Transmatrix matrix;
    public Rgba ambient; //preshade //was ambient
    public Rgba diffuse; //runtime //was diffuse
    public Rgba emissive; //ambient //was emissive
    public Rgba specular; //specular //was specular
    public int uvwSource;
    public Flt opacity; //float 0 to 1
    public Flt lodbias;
    public Flt specularPower; //a float with integer! values.
    public Uruobjectref texture;
    public Uruobjectref shader1;
    public Uruobjectref shader2;
    public Transmatrix identity;
    
    public x0006Layer(context c) throws readexception //,boolean hasHeader)
    {
        shared.IBytestream data = c.in;
        //if(hasHeader) xheader = new Objheader(c);
        parent = new x0041LayerInterface(c); //contains the plmipmap.
        //the next 5 flags are the hsGMatState
        flags1 = data.readInt(); //blend flags
        flags2 = data.readInt(); //clamp flags
        flags3 = data.readInt(); //shade flags
        flags4 = data.readInt(); //z flags
        flags5 = data.readInt(); //misc flags //there's a wireframe option here(0x1)!
        matrix = new Transmatrix(c); //transform
        ambient = new Rgba(c); //preshade
        diffuse = new Rgba(c); //runtime
        emissive = new Rgba(c); //ambient
        specular = new Rgba(c); //specular
        uvwSource = data.readInt(); //uvwsource
        opacity = new Flt(c); //opacity //float
        lodbias = new Flt(c); //LOD bias //float
        specularPower = new Flt(c); //Specular power //float
        texture = new Uruobjectref(c); //texture
        shader1 = new Uruobjectref(c); //vertex shader
        shader2 = new Uruobjectref(c); //pixel shader
        identity = new Transmatrix(c); //bumpEnvXfm
        
        
    }
    public void compile(Bytedeque data)
    {
        parent.compile(data);
        data.writeInt(flags1);
        data.writeInt(flags2);
        data.writeInt(flags3);
        data.writeInt(flags4);
        data.writeInt(flags5);
        matrix.compile(data);
        ambient.compile(data);
        diffuse.compile(data);
        emissive.compile(data);
        specular.compile(data);
        data.writeInt(uvwSource);
        opacity.compile(data);
        lodbias.compile(data);
        specularPower.compile(data);
        texture.compile(data);
        shader1.compile(data);
        shader2.compile(data);
        identity.compile(data);
        
    }
    private x0006Layer(){}
    public x0006Layer deepClone()
    {
        x0006Layer result =new x0006Layer();
        result.ambient = ambient.deepClone();
        result.diffuse = diffuse.deepClone();
        result.emissive = emissive.deepClone();
        result.flags1 = flags1;
        result.flags2 = flags2;
        result.flags3 = flags3;
        result.flags4 = flags4;
        result.flags5 = flags5;
        result.identity = identity.deepClone();
        result.lodbias = lodbias.deepClone();
        result.matrix = matrix.deepClone();
        result.opacity = opacity.deepClone();
        result.parent = parent.deepClone();
        result.shader1 = shader1.deepClone();
        result.shader2 = shader2.deepClone();
        result.specular = specular.deepClone();
        result.specularPower = specularPower.deepClone();
        result.texture = texture.deepClone();
        result.uvwSource = uvwSource;
        return result;
    }
    public static x0006Layer createEmpty()
    {
        x0006Layer r = new x0006Layer();
        r.parent = prpobjects.x0041LayerInterface.createEmpty();
        return r;
    }
}
