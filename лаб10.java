//Facade
class AudioSystem {
    public void turnOn() {
        System.out.println("Audio system is turned on.");
    }

    public void setVolume(int level) {
        System.out.println("Audio volume is set to " + level + ".");
    }

    public void turnOff() {
        System.out.println("Audio system is turned off.");
    }
}

class VideoProjector {
    public void turnOn() {
        System.out.println("Video projector is turned on.");
    }

    public void setResolution(String resolution) {
        System.out.println("Video resolution is set to " + resolution + ".");
    }

    public void turnOff() {
        System.out.println("Video projector is turned off.");
    }
}

class LightingSystem {
    public void turnOn() {
        System.out.println("Lights are turned on.");
    }

    public void setBrightness(int level) {
        System.out.println("Lights brightness is set to " + level + ".");
    }

    public void turnOff() {
        System.out.println("Lights are turned off.");
    }
}

class HomeTheaterFacade {
    private AudioSystem audioSystem;
    private VideoProjector videoProjector;
    private LightingSystem lightingSystem;

    public HomeTheaterFacade(AudioSystem audioSystem, VideoProjector videoProjector, LightingSystem lightingSystem) {
        this.audioSystem = audioSystem;
        this.videoProjector = videoProjector;
        this.lightingSystem = lightingSystem;
    }

    public void startMovie() {
        System.out.println("Preparing to start the movie...");
        lightingSystem.turnOn();
        lightingSystem.setBrightness(5);
        audioSystem.turnOn();
        audioSystem.setVolume(8);
        videoProjector.turnOn();
        videoProjector.setResolution("HD");
        System.out.println("Movie started.");
    }

    public void endMovie() {
        System.out.println("Shutting down movie...");
        videoProjector.turnOff();
        audioSystem.turnOff();
        lightingSystem.turnOff();
        System.out.println("Movie ended.");
    }
}

public class FacadeExample {
    public static void main(String[] args) {
        AudioSystem audio = new AudioSystem();
        VideoProjector video = new VideoProjector();
        LightingSystem lights = new LightingSystem();

        HomeTheaterFacade homeTheater = new HomeTheaterFacade(audio, video, lights);

        homeTheater.startMovie();
        System.out.println();
        homeTheater.endMovie();
    }
}
//2 Composite
import java.util.ArrayList;
import java.util.List;

abstract class FileSystemComponent {
    protected String name;

    public FileSystemComponent(String name) {
        this.name = name;
    }

    public abstract void display(int depth);

    public void add(FileSystemComponent component) {
        throw new UnsupportedOperationException("Cannot add to a leaf component.");
    }

    public void remove(FileSystemComponent component) {
        throw new UnsupportedOperationException("Cannot remove from a leaf component.");
    }
}

class File extends FileSystemComponent {
    public File(String name) {
        super(name);
    }

    @Override
    public void display(int depth) {
        System.out.println("  ".repeat(depth) + "- File: " + name);
    }
}

class Directory extends FileSystemComponent {
    private List<FileSystemComponent> children = new ArrayList<>();

    public Directory(String name) {
        super(name);
    }

    @Override
    public void add(FileSystemComponent component) {
        children.add(component);
    }

    @Override
    public void remove(FileSystemComponent component) {
        children.remove(component);
    }

    @Override
    public void display(int depth) {
        System.out.println("  ".repeat(depth) + "+ Directory: " + name);
        for (FileSystemComponent child : children) {
            child.display(depth + 1);
        }
    }
}

public class CompositeExample {
    public static void main(String[] args) {
        Directory root = new Directory("Root");
        File file1 = new File("File1.txt");
        File file2 = new File("File2.txt");

        Directory subDir = new Directory("SubDirectory");
        File subFile1 = new File("SubFile1.txt");

        root.add(file1);
        root.add(file2);
        subDir.add(subFile1);
        root.add(subDir);

        root.display(1);
    }
}
